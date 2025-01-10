package ust.tad.kubernetesmpsplugin.analysis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ust.tad.kubernetesmpsplugin.analysis.kubernetesparser.*;
import ust.tad.kubernetesmpsplugin.analysistask.AnalysisTaskResponseSender;
import ust.tad.kubernetesmpsplugin.analysistask.Location;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.KubernetesDeploymentModel;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.configStorageResources.ConfigMap;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.configStorageResources.PersistentVolumeClaim;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.ingress.KubernetesIngress;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.service.KubernetesService;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.workload.deployment.KubernetesDeployment;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.workload.pods.KubernetesPodSpec;
import ust.tad.kubernetesmpsplugin.models.ModelsService;
import ust.tad.kubernetesmpsplugin.models.tadm.InvalidPropertyValueException;
import ust.tad.kubernetesmpsplugin.models.tadm.TechnologyAgnosticDeploymentModel;
import ust.tad.kubernetesmpsplugin.models.tsdm.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

@Service
public class AnalysisService {
  @Autowired
  private ModelsService modelsService;

  @Autowired
  private AnalysisTaskResponseSender analysisTaskResponseSender;

  @Autowired
  private TransformationService transformationService;

  private static final Set<String> supportedFileExtensions = Set.of("yaml", "yml");

  private TechnologySpecificDeploymentModel tsdm;
  private TechnologyAgnosticDeploymentModel tadm;

  private final Set<KubernetesDeployment> deployments = new HashSet<>();
  private final Set<KubernetesService> services = new HashSet<>();
  private final Set<KubernetesPodSpec> pods = new HashSet<>();
  private final Set<KubernetesIngress> ingresses = new HashSet<>();
  private final Set<PersistentVolumeClaim> persistentVolumeClaims = new HashSet<>();
  private final Set<ConfigMap> configMaps = new HashSet<>();
  private final Set<Integer> newEmbeddedDeploymentModelIndexes = new HashSet<>();

  /**
   * Start the analysis of the deployment model. 1. Retrieve internal deployment models from models
   * service 2. Parse in technology-specific deployment model from locations 3. Update tsdm with new
   * information 4. Transform to EDMM entities and update tadm 5. Send updated models to models
   * service 6. Send AnalysisTaskResponse or EmbeddedDeploymentModelAnalysisRequests if present
   *
   * @param taskId
   * @param transformationProcessId
   * @param commands
   * @param locations
   */
  public void startAnalysis(
          UUID taskId,
          UUID transformationProcessId,
          List<String> commands,
          List<String> options,
          List<Location> locations) {
    this.newEmbeddedDeploymentModelIndexes.clear();
    this.deployments.clear();
    this.services.clear();

    TechnologySpecificDeploymentModel completeTsdm =
            modelsService.getTechnologySpecificDeploymentModel(transformationProcessId);
    this.tsdm = getExistingTsdm(completeTsdm, locations);
    if (tsdm == null) {
      analysisTaskResponseSender.sendFailureResponse(
              taskId, "No technology-specific deployment model found!");
      return;
    }
    this.tadm = modelsService.getTechnologyAgnosticDeploymentModel(transformationProcessId);

    try {
      runAnalysis(taskId, locations);
    } catch (URISyntaxException
             | IOException
             | InvalidNumberOfLinesException
             | InvalidAnnotationException
             | InvalidNumberOfContentException
             | InvalidPropertyValueException e) {
      e.printStackTrace();
      analysisTaskResponseSender.sendFailureResponse(taskId, e.getClass() + ": " + e.getMessage());
      return;
    }

    updateDeploymentModels(this.tsdm, this.tadm);

    if (newEmbeddedDeploymentModelIndexes.isEmpty()) {
      analysisTaskResponseSender.sendSuccessResponse(taskId);
    } else {
      for (int index : newEmbeddedDeploymentModelIndexes) {
        analysisTaskResponseSender.sendEmbeddedDeploymentModelAnalysisRequestFromModel(
                this.tsdm.getEmbeddedDeploymentModels().get(index), taskId);
      }
      analysisTaskResponseSender.sendSuccessResponse(taskId);
    }
  }

  private TechnologySpecificDeploymentModel getExistingTsdm(
          TechnologySpecificDeploymentModel tsdm, List<Location> locations) {
    for (DeploymentModelContent content : tsdm.getContent()) {
      for (Location location : locations) {
        if (location.getUrl().equals(content.getLocation())) {
          return tsdm;
        }
      }
    }
    for (TechnologySpecificDeploymentModel embeddedDeploymentModel :
            tsdm.getEmbeddedDeploymentModels()) {
      TechnologySpecificDeploymentModel foundModel =
              getExistingTsdm(embeddedDeploymentModel, locations);
      if (foundModel != null) {
        return foundModel;
      }
    }
    return null;
  }

  private void updateDeploymentModels(
          TechnologySpecificDeploymentModel tsdm, TechnologyAgnosticDeploymentModel tadm) {
    modelsService.updateTechnologySpecificDeploymentModel(tsdm);
    modelsService.updateTechnologyAgnosticDeploymentModel(tadm);
  }

  /**
   * Iterate over the locations and parse in all files that can be found. If the URL ends with a
   * ".", remove it. The file has to have a fileextension contained in the supported fileextension
   * Set, otherwise it will be ignored. If the given location is a directory, iterate over all
   * contained files. Removes the deployment model content associated with the old directory
   * locations because it has been resolved to the contained files.
   *
   * @param taskId the ID of the current analysis task.
   * @param locations the locations of the deployment models to analyze.
   * @throws InvalidNumberOfContentException
   * @throws InvalidAnnotationException
   * @throws InvalidNumberOfLinesException
   * @throws IOException
   * @throws URISyntaxException
   * @throws InvalidPropertyValueException
   */
  private void runAnalysis(UUID taskId, List<Location> locations)
          throws URISyntaxException,
          IOException,
          InvalidNumberOfLinesException,
          InvalidAnnotationException,
          InvalidNumberOfContentException,
          InvalidPropertyValueException {
    for (Location location : locations) {
      String locationURLString = location.getUrl().toString().trim().replaceAll("\\.$", "");
      URL locationURL = new URL(locationURLString);
      if ("file".equals(locationURL.getProtocol()) && new File(locationURL.toURI()).isDirectory()) {
        File directory = new File(locationURL.toURI());
        for (File file : Objects.requireNonNull(directory.listFiles())) {
          String fileExtension = StringUtils.getFilenameExtension(file.toURI().toURL().toString());
          if (fileExtension != null && supportedFileExtensions.contains(fileExtension)) {
            parseFile(file.toURI().toURL());
          }
        }
        DeploymentModelContent contentToRemove = new DeploymentModelContent();
        for (DeploymentModelContent content : this.tsdm.getContent()) {
          if (content.getLocation().equals(location.getUrl())) {
            contentToRemove = content;
          }
        }
        this.tsdm.removeDeploymentModelContent(contentToRemove);
      } else {
        String fileExtension = StringUtils.getFilenameExtension(locationURLString);
        if (fileExtension != null && supportedFileExtensions.contains(fileExtension)) {
          parseFile(locationURL);
        }
      }
    }
    this.tadm = transformationService.transformInternalToTADM(taskId, this.tadm,
            new KubernetesDeploymentModel(this.deployments, this.services, this.pods,
                    this.ingresses, this.persistentVolumeClaims, this.configMaps));
  }

  public void parseFile(URL url) throws IOException, InvalidNumberOfLinesException, InvalidAnnotationException {
    DeploymentModelContent deploymentModelContent = new DeploymentModelContent();
    deploymentModelContent.setLocation(url);

    List<Line> lines = new ArrayList<>();
    int lineNumber = 1;
    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
    while (reader.ready()) {
      String nextLine = reader.readLine();
      if (nextLine.startsWith("kind:")) {
        String kind = nextLine.split("kind:")[1].trim();
        kind = kind.split("#")[0].trim();
        List<String> readInLines = new ArrayList<>();
        readInLines.add(nextLine);
        int startLineNumber = lineNumber;
        while (reader.ready()) {
          nextLine = reader.readLine();
          if (nextLine.equals("---")) {
            break;
          }
          readInLines.add(nextLine);
          lineNumber++;
        }
        switch (kind) {
          case "Service":
            lines.addAll(ServiceParser.parseService(startLineNumber, readInLines, services));
            break;
          case "Deployment":
          case "StatefulSet":
          case "ReplicaSet":
            lines.addAll(DeploymentParser.parseDeployment(startLineNumber, readInLines,
                    deployments, persistentVolumeClaims));
            break;
          case "PersistentVolumeClaim":
            lines.addAll(PersistentVolumeClaimParser.parsePersistentVolumeClaim(startLineNumber,
                    readInLines, persistentVolumeClaims, Optional.empty()));
            break;
          case "ConfigMap":
            lines.addAll(ConfigMapParser.parseConfigMap(startLineNumber, readInLines, configMaps));
            break;
          case "Ingress":
            lines.addAll(IngressParser.parseIngress(startLineNumber, readInLines, ingresses));
            break;
          default:
            lines.addAll(createLinesForUnknownType(lineNumber, readInLines));
            break;
        }
      }
      lineNumber++;
    }
    reader.close();
    if (!lines.isEmpty()) {
      deploymentModelContent.setLines(lines);
      this.tsdm.addDeploymentModelContent(deploymentModelContent);
    }
  }

  private List<Line> createLinesForUnknownType(int lineNumber, List<String> readInLines) throws InvalidAnnotationException {
    List<Line> lines = new ArrayList<>();
    for (int i = lineNumber; i <= readInLines.size() + lineNumber; i++) {
      lines.add(new Line(i, 0D, true));
    }
    return lines;
  }
}
