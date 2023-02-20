package ust.tad.kubernetesmpsplugin.analysis;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class RunMPSProjectTest {

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
}

