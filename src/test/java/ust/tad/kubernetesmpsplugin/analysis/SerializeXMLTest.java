package ust.tad.kubernetesmpsplugin.analysis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.KubernetesDeploymentModel;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.deployment.*;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.service.KubernetesService;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.service.Selector;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.service.ServicePort;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class SerializeXMLTest {

    @Value("${mps.inputModel.path}")
    private String mpsInputPath;

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

    private KubernetesDeploymentModel createDummyModel() {
        Label label = new Label("app", "dummyApp");
        ContainerPort containerPort = new ContainerPort("containerPort", 8080);
        EnvironmentVariable environmentVariable = new EnvironmentVariable("dummyEnv", "dummyEnvVal");
        Container dummyContainer = new Container("dummyContainer",
                "dummyRepo/dummy:main", Set.of(containerPort), Set.of(environmentVariable));
        KubernetesDeployment dummyDeployment = new KubernetesDeployment("dummyDeployment",
                3, Set.of(label), Set.of(dummyContainer));
        KubernetesDeployment dummyDeploymentTwo = new KubernetesDeployment("dummyDeploymentTwo",
                1, Set.of(label), Set.of(dummyContainer));

        ServicePort servicePort = new ServicePort("defaultPort", 80, "8080");
        Selector selector = new Selector("app", "dummyApp");
        KubernetesService dummyService = new KubernetesService("dummyService",
                Set.of(servicePort), Set.of(selector));

        return new KubernetesDeploymentModel(
                Set.of(dummyDeployment, dummyDeploymentTwo),
                Set.of(dummyService));
    }

}
