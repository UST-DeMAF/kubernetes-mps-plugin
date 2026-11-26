package ust.tad.kubernetesmpsplugin.analysis.kubernetesparser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.configStorageResources.PersistentVolumeClaim;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.configStorageResources.Volume;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.workload.deployment.KubernetesDeployment;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.workload.pods.KubernetesPodSpec;
import ust.tad.kubernetesmpsplugin.models.tsdm.Line;

public class PodSpecParserTest {

  @Test
  public void parsePod_volumesWithPersistentVolumeClaims() {
    List<String> pod =
        List.of(
            "    spec:",
            "      volumes:",
            "        - name: data",
            "          persistentVolumeClaim:",
            "            claimName: app-data",
            "        - name: logs",
            "          persistentVolumeClaim:",
            "            claimName: app-logs",
            "        - name: cache",
            "          emptyDir: {}",
            "      restartPolicy: Always",
            "      containers:",
            "        - name: app",
            "          image: nginx");
    KubernetesDeployment deployment = new KubernetesDeployment();

    List<Line> lines =
        assertTimeoutPreemptively(
            Duration.ofSeconds(5), () -> PodSpecParser.parsePod(7, pod, deployment));

    KubernetesPodSpec podSpec = deployment.getPodSpecs().iterator().next();
    Map<String, String> claims = new HashMap<>();
    for (Volume volume : podSpec.getVolumes()) {
      claims.put(volume.getName(), volume.getPersistentVolumeClaimName());
    }
    assertEquals(Set.of("data", "logs", "cache"), claims.keySet());
    assertEquals("app-data", claims.get("data"));
    assertEquals("app-logs", claims.get("logs"));
    assertEquals("Always", podSpec.getRestartPolicy());
    assertEquals(1, podSpec.getContainers().size());
    assertEquals(
        Set.of(7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20),
        lines.stream().map(Line::getNumber).collect(Collectors.toSet()));
  }

  @Test
  public void parseDeployment_volumeClaimTemplatesNull() {
    List<String> statefulSet =
        List.of(
            "apiVersion: apps/v1",
            "kind: StatefulSet",
            "metadata:",
            "  name: db",
            "spec:",
            "  volumeClaimTemplates: null",
            "  template:",
            "    spec:",
            "      containers:",
            "        - name: db",
            "          image: postgres");
    Set<KubernetesDeployment> deployments = new HashSet<>();
    Set<PersistentVolumeClaim> pvcs = new HashSet<>();

    assertTimeoutPreemptively(
        Duration.ofSeconds(5),
        () -> DeploymentParser.parseDeployment(1, statefulSet, deployments, pvcs));

    assertEquals(0, pvcs.size());
    assertEquals(1, deployments.size());
  }
}
