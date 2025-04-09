package ust.tad.kubernetesmpsplugin.models;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.KubernetesDeploymentModel;
import ust.tad.kubernetesmpsplugin.models.tadm.TechnologyAgnosticDeploymentModel;
import ust.tad.kubernetesmpsplugin.models.tsdm.TechnologySpecificDeploymentModel;

@Service
public class ModelsService {

  private static final Logger LOG = LoggerFactory.getLogger(ModelsService.class);

  @Autowired private WebClient modelsServiceApiClient;

  /**
   * Retrieve a technology-specific deployment model from the model service.
   *
   * @param transformationProcessId the identifier of the technology-specific deployment model.
   * @return the technology-specific deployment model.
   */
  public TechnologySpecificDeploymentModel getTechnologySpecificDeploymentModel(
      UUID transformationProcessId) {
    LOG.info("Requesting technology-specific deployment model");
    return modelsServiceApiClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder.path("/technology-specific/" + transformationProcessId).build())
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(TechnologySpecificDeploymentModel.class)
        .block();
  }

  /**
   * Update a technology-specific deployment model by sending it to the update endpoint of the
   * models service.
   *
   * @param technologySpecificDeploymentModel the technology-specific deployment model to update.
   */
  public void updateTechnologySpecificDeploymentModel(
      TechnologySpecificDeploymentModel technologySpecificDeploymentModel) {
    LOG.info("Updating technology-specific deployment model");
    modelsServiceApiClient
        .post()
        .uri("/technology-specific")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(technologySpecificDeploymentModel))
        .retrieve()
        .bodyToMono(TechnologySpecificDeploymentModel.class)
        .block();
  }

  /**
   * Retrieve a technology-agnostic deployment model from the model service.
   *
   * @param transformationProcessId the identifier of the technology-agnostic deployment model.
   * @return the technology-agnostic deployment model.
   */
  public TechnologyAgnosticDeploymentModel getTechnologyAgnosticDeploymentModel(
      UUID transformationProcessId) {
    LOG.info("Requesting technology-agnostic deployment model");
    return modelsServiceApiClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder.path("/technology-agnostic/" + transformationProcessId).build())
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(TechnologyAgnosticDeploymentModel.class)
        .block();
  }

  /**
   * Update a technology-agnostic deployment model by sending it to the update endpoint of the
   * models service.
   *
   * @param technologyAgnosticDeploymentModel the technology-agnostic deployment model to update.
   */
  public void updateTechnologyAgnosticDeploymentModel(
      TechnologyAgnosticDeploymentModel technologyAgnosticDeploymentModel) {
    LOG.info("Updating technology-agnostic deployment model");
    modelsServiceApiClient
        .post()
        .uri("/technology-agnostic")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(technologyAgnosticDeploymentModel))
        .retrieve()
        .bodyToMono(TechnologyAgnosticDeploymentModel.class)
        .block();
  }


  /**
   * Retrieve a kubernetes deployment model from the model service.
   *
   * @param transformationProcessId the identifier of the kubernetes deployment model.
   * @return the kubernetes deployment model.
   */
  public KubernetesDeploymentModel getKubernetesDeploymentModel(
          UUID transformationProcessId) {
    LOG.info("Requesting kubernetes deployment model");
    return modelsServiceApiClient
            .get()
            .uri(
                    uriBuilder ->
                            uriBuilder.path("/kubernetes/" + transformationProcessId).build())
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(KubernetesDeploymentModel.class)
            .block();
  }

  /**
   * Update a kubernetes deployment model by sending it to the update endpoint of the
   * models service.
   *
   * @param kubernetesDeploymentModel the kubernetesDeploymentModel to update.
   */
  public void updateKubernetesDeploymentModel(
          KubernetesDeploymentModel kubernetesDeploymentModel) {
    LOG.info("Updating kubernetes deployment model");
    modelsServiceApiClient
            .post()
            .uri("/kubernetes")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(kubernetesDeploymentModel))
            .retrieve()
            .bodyToMono(KubernetesDeploymentModel.class)
            .block();
  }
}
