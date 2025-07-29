package ust.tad.kubernetesmpsplugin.analysis;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.KubernetesDeploymentModel;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.common.types.StringStringMap;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.configStorageResources.PersistentVolumeClaim;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.configStorageResources.Volume;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.service.KubernetesService;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.service.ServicePort;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.workload.deployment.KubernetesDeployment;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.workload.pods.*;
import ust.tad.kubernetesmpsplugin.models.tadm.*;

public class SerializeXMLTest {

  @Test
  public void serializeKubernetesToXML() throws JsonProcessingException {
    KubernetesDeploymentModel modelToSerialize = createDummyModel();

    XmlMapper xmlMapper = new XmlMapper();
    String xml = xmlMapper.writeValueAsString(modelToSerialize);
    assertNotNull(xml);
    System.out.print(xml);
  }

  @Test
  public void serializeKubernetesToXMLFile() throws IOException {
    KubernetesDeploymentModel modelToSerialize = createDummyModel();

    XmlMapper xmlMapper = new XmlMapper();
    xmlMapper.writeValue(new File("dummyKubeDM.xml"), modelToSerialize);
    File file = new File("dummyKubeDM.xml");
    assertNotNull(file);
  }

  @Test
  public void serializeTADMToXMLFile() throws IOException, InvalidPropertyValueException, InvalidRelationException {
    TechnologyAgnosticDeploymentModel modelToSerialize = createDummyExistingTADM();

    XmlMapper xmlMapper = new XmlMapper();
    xmlMapper.writeValue(new File("dummyExistingTADM.xml"), modelToSerialize);
    File file = new File("dummyExistingTADM.xml");
    assertNotNull(file);
  }

  private TechnologyAgnosticDeploymentModel createDummyExistingTADM() throws InvalidPropertyValueException, InvalidRelationException {
    TechnologyAgnosticDeploymentModel tadm = new TechnologyAgnosticDeploymentModel();
    RelationType dependsOn = new RelationType();
    dependsOn.setName("DependsOn");
    RelationType hostedOn = new RelationType();
    hostedOn.setName("HostedOn");
    hostedOn.setParentType(dependsOn);
    RelationType connectsTo = new RelationType();
    connectsTo.setName("ConnectsTo");
    connectsTo.setParentType(dependsOn);

    ComponentType baseType = new ComponentType();
    baseType.setName("BaseType");
    ComponentType containerPlatform = new ComponentType("ContainerPlatform", null, null, null, baseType);
    containerPlatform.setName("ContainerPlatform");
    containerPlatform.setParentType(baseType);
    ComponentType cluster = new ComponentType("KubernetesCluster", null, null, null, containerPlatform);
    cluster.setName("KubernetesCluster");
    cluster.setParentType(containerPlatform);
    ComponentType cpCluster = new ComponentType("CloudProviderCluster", null, null, null, cluster);
    cpCluster.setName("CloudProviderCluster");
    cpCluster.setParentType(cluster);

    Component myCluster = new Component();
    myCluster.setName("myCluster");
    myCluster.setType(cpCluster);

    ComponentType databaseType = new ComponentType();
    databaseType.setName("DatabaseType");
    databaseType.setParentType(baseType);

    Component database = new Component();
    database.setName("myDatabase");
    database.setType(databaseType);
    Property dbProp = new Property("dbName", PropertyType.STRING, false, "testDB", Confidence.CONFIRMED);
    database.setProperties(List.of(dbProp));
    databaseType.setProperties(List.of(dbProp));

    ComponentType frontendType = new ComponentType();
    frontendType.setName("FrontendType");
    frontendType.setParentType(baseType);

    Component frontend = new Component();
    frontend.setName("frontend");
    frontend.setType(frontendType);
    Property frontendProp = new Property("name", PropertyType.STRING, false, "frontend", Confidence.CONFIRMED);
    Property frontendPropConnect = new Property("connectToBackend", PropertyType.STRING, false, "https://dummyService:80", Confidence.CONFIRMED);
    frontend.setProperties(List.of(frontendProp, frontendPropConnect));
    frontendType.setProperties(List.of(frontendProp, frontendPropConnect));
    Artifact frontendArtifact = new Artifact();
    frontendArtifact.setType("docker_image");
    frontendArtifact.setName("frontend:1.2.3");
    frontend.setArtifacts(List.of(frontendArtifact));

    Relation frontendToDB = new Relation();
    frontendToDB.setType(connectsTo);
    frontendToDB.setName("frontend_ConnectsTo_database");
    frontendToDB.setSource(frontend);
    frontendToDB.setTarget(database);

    return new TechnologyAgnosticDeploymentModel(UUID.randomUUID(), List.of(),
            List.of(myCluster, database, frontend),
            List.of(frontendToDB),
            List.of(baseType, containerPlatform, cluster, cpCluster, databaseType, frontendType),
            List.of(dependsOn, hostedOn, connectsTo));
  }

  private KubernetesDeploymentModel createDummyModel() {
    StringStringMap label = new StringStringMap("app", "dummyApp");
    ContainerPort containerPort = new ContainerPort("containerPort", 8080);
    EnvironmentVariable environmentVariable = new EnvironmentVariable("dummyEnv", "dummyEnvVal");
    VolumeMount volumeMount = new VolumeMount();
    volumeMount.setMountPath("/dir");
    volumeMount.setName("storageVolume");
    Container dummyContainer =
            new Container(
                    "dummyContainer",
                    "dummyRepo/dummy:main",
                    "never",
                    List.of(),
                    List.of(),
                    "/",
                    Set.of(containerPort),
                    Set.of(environmentVariable),
                    Set.of(volumeMount));
    Volume volume = new Volume("storageVolume", "pvcName", true);
    KubernetesPodSpec pod = new KubernetesPodSpec("testPod",
            "never",
            Set.of(dummyContainer),
            Set.of(),
            Set.of(),
            Set.of(volume));
    KubernetesDeployment dummyDeployment =
            new KubernetesDeployment("dummyDeployment", 3, Set.of(label), Set.of(label), Set.of(pod));
    KubernetesDeployment dummyDeploymentTwo =
            new KubernetesDeployment("dummyDeploymentTwo", 1, Set.of(label), Set.of(label), Set.of(pod));

    ServicePort servicePort = new ServicePort("defaultPort", 80, "8080");
    StringStringMap selector = new StringStringMap("app", "dummyApp");
    KubernetesService dummyService =
            new KubernetesService("dummyService",
                    "ClusterIP",
                    "test", "test", "test", "test", "test", "test", "test", 1,
                    Set.of(servicePort), Set.of(selector),
                    Set.of(), Set.of(), Set.of(), Set.of());

    PersistentVolumeClaim pvc = new PersistentVolumeClaim("pvcName", "", "1Gi","3" );

    return new KubernetesDeploymentModel(UUID.randomUUID(),
            Set.of(dummyDeployment, dummyDeploymentTwo), Set.of(dummyService), Set.of(), Set.of(),
            Set.of(pvc), Set.of());
  }
}
