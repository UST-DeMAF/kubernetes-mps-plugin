package ust.tad.kubernetesmpsplugin.analysis.kubernetesparser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.ingress.HTTPIngressRuleValue;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.ingress.IngressBackend;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.ingress.IngressRule;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.ingress.KubernetesIngress;
import ust.tad.kubernetesmpsplugin.models.tsdm.InvalidAnnotationException;
import ust.tad.kubernetesmpsplugin.models.tsdm.Line;

public class IngressParserTest {

  private static KubernetesIngress parse(List<String> readInLines)
      throws InvalidAnnotationException {
    Set<KubernetesIngress> ingresses = new HashSet<>();
    IngressParser.parseIngress(1, readInLines, ingresses);
    assertEquals(1, ingresses.size());
    return ingresses.iterator().next();
  }

  @Test
  public void parseIngress_listItemsIndentedFurther_success() throws InvalidAnnotationException {
    KubernetesIngress ingress =
        parse(
            List.of(
                "kind: Ingress",
                "metadata:",
                "  name: web",
                "spec:",
                "  ingressClassName: nginx",
                "  rules:",
                "    - host: shop.example.com",
                "      http:",
                "        paths:",
                "          - path: /",
                "            pathType: Prefix",
                "            backend:",
                "              service:",
                "                name: frontend",
                "                port:",
                "                  name: http"));

    assertEquals("nginx", ingress.getIngressClassName());
    assertEquals(1, ingress.getRules().size());
    IngressRule rule = ingress.getRules().get(0);
    assertEquals("shop.example.com", rule.getHost());
    assertEquals(1, rule.getIngressRuleValues().size());
    HTTPIngressRuleValue path = rule.getIngressRuleValues().get(0);
    assertEquals("/", path.getPath());
    assertEquals("Prefix", path.getPathType());
    IngressBackend backend = path.getIngressBackend();
    assertEquals("frontend", backend.getServiceName());
    assertEquals("http", backend.getServicePortName());
  }

  @Test
  public void parseIngress_listItemsAtSameIndentation_success()
      throws InvalidAnnotationException {
    KubernetesIngress ingress =
        parse(
            List.of(
                "kind: Ingress",
                "metadata:",
                "  name: web",
                "spec:",
                "  ingressClassName: nginx",
                "  rules:",
                "  - host: shop.example.com",
                "    http:",
                "      paths:",
                "      - path: /",
                "        pathType: Prefix",
                "        backend:",
                "          service:",
                "            name: frontend",
                "            port:",
                "              number: 8080"));

    assertEquals("nginx", ingress.getIngressClassName());
    assertEquals(1, ingress.getRules().size());
    IngressRule rule = ingress.getRules().get(0);
    assertEquals("shop.example.com", rule.getHost());
    assertEquals(1, rule.getIngressRuleValues().size());
    HTTPIngressRuleValue path = rule.getIngressRuleValues().get(0);
    assertEquals("/", path.getPath());
    assertEquals("Prefix", path.getPathType());
    IngressBackend backend = path.getIngressBackend();
    assertEquals("frontend", backend.getServiceName());
    assertEquals(8080, backend.getServicePortPort());
    assertNull(backend.getServicePortName());
  }

  @Test
  public void parseIngress_multipleRulesAndPaths_success() throws InvalidAnnotationException {
    KubernetesIngress ingress =
        parse(
            List.of(
                "kind: Ingress",
                "metadata:",
                "  name: web",
                "spec:",
                "  rules:",
                "    # rule for the shop",
                "    - host: shop.example.com",
                "      http:",
                "        paths:",
                "          - path: /api",
                "            pathType: Prefix",
                "            backend:",
                "              service:",
                "                name: backend",
                "                port:",
                "                  number: 8080",
                "",
                "          - path: /",
                "            pathType: Prefix",
                "            backend:",
                "              service:",
                "                name: frontend",
                "                port:",
                "                  number: 80",
                "    - http:",
                "        paths:",
                "          - path: /",
                "            pathType: Exact",
                "            backend:",
                "              service:",
                "                name: default",
                "                port:",
                "                  name: http"));

    assertNull(ingress.getIngressClassName());
    assertEquals(2, ingress.getRules().size());

    IngressRule shopRule = ingress.getRules().get(0);
    assertEquals("shop.example.com", shopRule.getHost());
    assertEquals(2, shopRule.getIngressRuleValues().size());
    assertEquals("/api", shopRule.getIngressRuleValues().get(0).getPath());
    assertEquals(
        "backend", shopRule.getIngressRuleValues().get(0).getIngressBackend().getServiceName());
    assertEquals(
        8080, shopRule.getIngressRuleValues().get(0).getIngressBackend().getServicePortPort());
    assertEquals("/", shopRule.getIngressRuleValues().get(1).getPath());
    assertEquals(
        "frontend", shopRule.getIngressRuleValues().get(1).getIngressBackend().getServiceName());
    assertEquals(
        80, shopRule.getIngressRuleValues().get(1).getIngressBackend().getServicePortPort());

    IngressRule defaultRule = ingress.getRules().get(1);
    assertNull(defaultRule.getHost());
    assertEquals(1, defaultRule.getIngressRuleValues().size());
    assertEquals("Exact", defaultRule.getIngressRuleValues().get(0).getPathType());
    assertEquals(
        "default", defaultRule.getIngressRuleValues().get(0).getIngressBackend().getServiceName());
    assertEquals(
        "http", defaultRule.getIngressRuleValues().get(0).getIngressBackend().getServicePortName());
  }

  @Test
  public void parseIngress_v1beta1Backend_success() throws InvalidAnnotationException {
    KubernetesIngress ingress =
        parse(
            List.of(
                "kind: Ingress",
                "spec:",
                "  rules:",
                "  - host: shop.example.com",
                "    http:",
                "      paths:",
                "      - path: /",
                "        backend:",
                "          serviceName: frontend",
                "          servicePort: 80"));

    IngressBackend backend =
        ingress.getRules().get(0).getIngressRuleValues().get(0).getIngressBackend();
    assertEquals("frontend", backend.getServiceName());
    assertEquals(80, backend.getServicePortPort());
  }

  @Test
  public void parseIngress_noRules_success() throws InvalidAnnotationException {
    KubernetesIngress ingress =
        parse(List.of("kind: Ingress", "metadata:", "  name: web", "spec:", "  ingressClassName: nginx"));

    assertEquals("nginx", ingress.getIngressClassName());
    assertEquals(0, ingress.getRules().size());
  }

  @Test
  public void parseIngress_annotatesEachLine() throws InvalidAnnotationException {
    List<String> readInLines =
        List.of(
            "kind: Ingress",
            "metadata:",
            "  name: web",
            "spec:",
            "  # comment",
            "  ingressClassName: nginx",
            "  tls:",
            "    - hosts:",
            "        - shop.example.com",
            "  rules:",
            "    - host: shop.example.com");

    List<Line> lines = IngressParser.parseIngress(5, readInLines, new HashSet<>());

    assertEquals(readInLines.size(), lines.size());
    for (int index = 0; index < lines.size(); index++) {
      assertEquals(5 + index, lines.get(index).getNumber());
    }
    double[] expectedComprehensibility = {1D, 0D, 0D, 1D, 1D, 1D, 0D, 0D, 0D, 1D, 1D};
    for (int index = 0; index < lines.size(); index++) {
      assertEquals(
          expectedComprehensibility[index],
          lines.get(index).getComprehensibility(),
          "Comprehensibility of line " + readInLines.get(index));
    }
  }
}
