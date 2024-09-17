package ust.tad.kubernetesmpsplugin.analysis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import ust.tad.kubernetesmpsplugin.models.tadm.TechnologyAgnosticDeploymentModel;

@SpringBootTest
public class RunMPSProjectTest {
  @Value("${mps.result.path}")
  private String mpsOutputPath;

  @Test
  public void runProject() {
    String prepareMpsCommand =
        "./mps-transformation-kubernetes/gradlew -p mps-transformation-kubernetes prepareMps";
    String mpsBuildCommand =
        "./mps-transformation-kubernetes/gradlew -p mps-transformation-kubernetes mpsBuild";
    try {
      Runtime.getRuntime().exec(prepareMpsCommand);
      Runtime.getRuntime().exec(mpsBuildCommand);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void runProjectApacheCommonsExec() throws IOException {
    CommandLine prepareMps =
        CommandLine.parse(
            "./mps-transformation-kubernetes/gradlew -p mps-transformation-kubernetes prepareMps");
    CommandLine mpsBuild =
        CommandLine.parse(
            "./mps-transformation-kubernetes/gradlew -p mps-transformation-kubernetes mpsBuild");
    DefaultExecutor executor = new DefaultExecutor();
    int exitValuePrepareMps = executor.execute(prepareMps);
    System.out.println(exitValuePrepareMps);
    int exitValueMpsBuild = executor.execute(mpsBuild);
    System.out.println(exitValueMpsBuild);
  }

  @Test
  public void deserializeYaml() throws IOException {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    mapper.findAndRegisterModules();
    TechnologyAgnosticDeploymentModel newTADM =
        mapper.readValue(new File(mpsOutputPath), TechnologyAgnosticDeploymentModel.class);
    System.out.println(newTADM.toString());
  }
}
