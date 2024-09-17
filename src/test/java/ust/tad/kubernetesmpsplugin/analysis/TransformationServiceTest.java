package ust.tad.kubernetesmpsplugin.analysis;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.KubernetesDeploymentModel;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.deployment.*;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.service.KubernetesService;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.service.Selector;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.service.ServicePort;
import ust.tad.kubernetesmpsplugin.models.tadm.TechnologyAgnosticDeploymentModel;

@SpringBootTest
public class TransformationServiceTest {

  @Autowired TransformationService transformationService;

  @Test
  public void runTransformationSuccessful() throws IOException {
    KubernetesDeploymentModel inputModel = createDummyModel();
    TechnologyAgnosticDeploymentModel result =
        transformationService.transformInternalToTADM(
            new TechnologyAgnosticDeploymentModel(), inputModel);
    assertNotNull(result);
    System.out.println(result);
  }

  private KubernetesDeploymentModel createDummyModel() {
    Label label = new Label("app", "dummyApp");
    ContainerPort containerPort = new ContainerPort("containerPort", 8080);
    EnvironmentVariable environmentVariable = new EnvironmentVariable("dummyEnv", "dummyEnvVal");
    Container dummyContainer =
        new Container(
            "dummyContainer",
            "dummyRepo/dummy:main",
            Set.of(containerPort),
            Set.of(environmentVariable));
    KubernetesDeployment dummyDeployment =
        new KubernetesDeployment("dummyDeployment", 3, Set.of(label), Set.of(dummyContainer));
    KubernetesDeployment dummyDeploymentTwo =
        new KubernetesDeployment("dummyDeploymentTwo", 1, Set.of(label), Set.of(dummyContainer));

    ServicePort servicePort = new ServicePort("defaultPort", 80, "8080");
    Selector selector = new Selector("app", "dummyApp");
    KubernetesService dummyService =
        new KubernetesService("dummyService", Set.of(servicePort), Set.of(selector));

    return new KubernetesDeploymentModel(
        Set.of(dummyDeployment, dummyDeploymentTwo), Set.of(dummyService));
  }
}
