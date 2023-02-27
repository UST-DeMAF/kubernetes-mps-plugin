package ust.tad.kubernetesmpsplugin.analysis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import ust.tad.kubernetesmpsplugin.models.tadm.TechnologyAgnosticDeploymentModel;

import java.io.File;
import java.io.IOException;

@SpringBootTest
public class RunMPSProjectTest {


    @Value("${mps.result.path}")
    private String mpsOutputPath;

    @Test
    public void runProject() {
        String prepareMpsCommand = "./mps-transformation/gradlew -p mps-transformation prepareMps";
        String mpsBuildCommand = "./mps-transformation/gradlew -p mps-transformation mpsBuild";
        try {
            Runtime.getRuntime().exec(prepareMpsCommand);
            Runtime.getRuntime().exec(mpsBuildCommand);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void runProjectApacheCommonsExec() throws IOException {
        CommandLine prepareMps = CommandLine.parse("./mps-transformation/gradlew -p mps-transformation prepareMps");
        CommandLine mpsBuild = CommandLine.parse("./mps-transformation/gradlew -p mps-transformation mpsBuild");
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
        TechnologyAgnosticDeploymentModel newTADM = mapper.readValue(
                new File(mpsOutputPath), TechnologyAgnosticDeploymentModel.class);
        System.out.println(newTADM.toString());
    }
}

