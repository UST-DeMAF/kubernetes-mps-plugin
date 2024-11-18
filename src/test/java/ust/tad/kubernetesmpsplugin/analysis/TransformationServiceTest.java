package ust.tad.kubernetesmpsplugin.analysis;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.KubernetesDeploymentModel;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.common.types.StringStringMap;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.service.KubernetesService;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.service.ServicePort;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.workload.deployment.KubernetesDeployment;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.workload.pods.Container;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.workload.pods.ContainerPort;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.workload.pods.EnvironmentVariable;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.workload.pods.KubernetesPodSpec;
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
    StringStringMap label = new StringStringMap("app", "dummyApp");
    ContainerPort containerPort = new ContainerPort("containerPort", 8080);
    EnvironmentVariable environmentVariable = new EnvironmentVariable("dummyEnv", "dummyEnvVal");
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
                    Set.of());
    KubernetesPodSpec pod = new KubernetesPodSpec("testPod",
            "never",
            Set.of(dummyContainer),
            Set.of(),
            Set.of());
    KubernetesDeployment dummyDeployment =
            new KubernetesDeployment("dummyDeployment", 3, Set.of(label), Set.of(pod));
    KubernetesDeployment dummyDeploymentTwo =
            new KubernetesDeployment("dummyDeploymentTwo", 1, Set.of(label), Set.of(pod));

    ServicePort servicePort = new ServicePort("defaultPort", 80, "8080");
    StringStringMap selector = new StringStringMap("app", "dummyApp");
    KubernetesService dummyService =
            new KubernetesService("dummyService",
                    "ClusterIP",
                    "test", "test", "test", "test", "test", "test", "test", 1,
                    Set.of(servicePort), Set.of(selector),
                    Set.of(), Set.of(), Set.of(), Set.of());

    return new KubernetesDeploymentModel(
            Set.of(dummyDeployment, dummyDeploymentTwo), Set.of(dummyService));
  }
}
