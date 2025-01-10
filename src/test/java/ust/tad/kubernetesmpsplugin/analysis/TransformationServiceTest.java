package ust.tad.kubernetesmpsplugin.analysis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.KubernetesDeploymentModel;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.common.types.StringStringMap;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.configStorageResources.PersistentVolumeClaim;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.configStorageResources.Volume;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.service.KubernetesService;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.service.ServicePort;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.workload.deployment.KubernetesDeployment;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.workload.pods.*;
import ust.tad.kubernetesmpsplugin.models.tadm.TechnologyAgnosticDeploymentModel;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class TransformationServiceTest {

  @Autowired TransformationService transformationService;

  @Test
  public void runTransformationSuccessful() throws IOException {
    KubernetesDeploymentModel inputModel = createDummyModel();
    TechnologyAgnosticDeploymentModel result =
            transformationService.transformInternalToTADM(UUID.randomUUID(),
                    new TechnologyAgnosticDeploymentModel(), inputModel);
    assertNotNull(result);
    System.out.println(result);
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

    PersistentVolumeClaim pvc = new PersistentVolumeClaim("pvcName", "", "1Gi","3" );

    return new KubernetesDeploymentModel(
            Set.of(dummyDeployment, dummyDeploymentTwo), Set.of(dummyService), Set.of(), Set.of(),
            Set.of(pvc), Set.of());
  }
}
