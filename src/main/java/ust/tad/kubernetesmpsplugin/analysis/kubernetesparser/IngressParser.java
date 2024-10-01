package ust.tad.kubernetesmpsplugin.analysis.kubernetesparser;

import ust.tad.kubernetesmpsplugin.kubernetesmodel.common.types.StringStringMap;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.configStorageResources.ConfigMap;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.ingress.HTTPIngressRuleValue;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.ingress.IngressBackend;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.ingress.IngressRule;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.ingress.KubernetesIngress;
import ust.tad.kubernetesmpsplugin.models.tsdm.InvalidAnnotationException;
import ust.tad.kubernetesmpsplugin.models.tsdm.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class IngressParser {
    public static List<Line> parseIngress(int lineNumber, List<String> readInLines, Set<KubernetesIngress> ingresses) throws InvalidAnnotationException {
        List<Line> lines = new ArrayList<>();
        KubernetesIngress ingress = new KubernetesIngress();
        ListIterator<String> iterator = readInLines.listIterator();

        while (iterator.hasNext()) {
            String currentLine = iterator.next();
            if (currentLine.trim().startsWith("spec:")) {
                lineNumber++;
                lines.add(new Line(lineNumber, 1D, true));

                while (iterator.hasNext() && (currentLine = iterator.next()).startsWith("  ")) {
                    lineNumber++;
                    if (currentLine.trim().startsWith("ingressClassName:")) {
                        lines.add(new Line(lineNumber, 1D, true));
                        ingress.setIngressClassName(currentLine.trim().split(":")[1]);
                    } else if (currentLine.trim().startsWith("rules:")) {
                        lines.add(new Line(lineNumber, 1D, true));
                        while (iterator.hasNext() && (currentLine = iterator.next()).startsWith("    -")) {
                            lineNumber++;
                            IngressRule rule = new IngressRule();
                            if (currentLine.replace("-", "").trim().startsWith("host:")) {
                                lines.add(new Line(lineNumber, 1D, true));
                                rule.setHost(currentLine.trim().split(":")[1]);
                            } else if (currentLine.trim().startsWith("paths:")) {
                                lines.add(new Line(lineNumber, 1D, true));
                                HTTPIngressRuleValue ruleValue = new HTTPIngressRuleValue();
                                while(iterator.hasNext() && (currentLine = iterator.next()).startsWith("      -")) {
                                    lineNumber++;
                                    if (currentLine.replace("-", "").trim().startsWith("path:")) {
                                        lines.add(new Line(lineNumber, 1D, true));
                                        ruleValue.setPath(currentLine.trim().split(":")[1]);
                                    } else if (currentLine.replace("-", "").trim().startsWith("pathType:")) {
                                        lines.add(new Line(lineNumber, 1D, true));
                                        ruleValue.setPathType(currentLine.trim().split(":")[1]);
                                    } else if (currentLine.replace("-", "").trim().startsWith("backend:")) {
                                        lines.add(new Line(lineNumber, 1D, true));
                                        IngressBackend backend = new IngressBackend();
                                        while (iterator.hasNext() && (currentLine = iterator.next()).startsWith("        ")) {
                                            lineNumber++;
                                            if (currentLine.trim().startsWith("name:")) {
                                                lines.add(new Line(lineNumber, 1D, true));
                                                backend.setServiceName(currentLine.trim().split(":")[1]);
                                            } else if (currentLine.trim().startsWith("port:")) {
                                                lines.add(new Line(lineNumber, 1D, true));
                                                lineNumber++;
                                                currentLine = iterator.next();
                                                if (currentLine.trim().startsWith("name:")) {
                                                    lines.add(new Line(lineNumber, 1D, true));
                                                    backend.servicePortName(currentLine.trim().split(":")[1]);
                                                } else if (currentLine.trim().startsWith("port:")) {
                                                    lines.add(new Line(lineNumber, 1D, true));
                                                    backend.servicePortPort(Integer.parseInt(currentLine.trim().split(":")[1]));
                                                } else {
                                                    lines.add(new Line(lineNumber, 0D, true));
                                                }
                                            } else {
                                                lines.add(new Line(lineNumber, 0D, true));
                                            }
                                        }

                                        ruleValue.setIngressBackend(backend);
                                    }
                                }
                                rule.getIngressRuleValues().add(ruleValue);
                            } else {
                                lines.add(new Line(lineNumber, 0D, true));
                            }
                            ingress.getRules().add(rule);
                        }
                        if (iterator.hasNext()) iterator.previous();

                    } else {
                        lines.add(new Line(lineNumber, 0D, true));
                    }
                }
            } else {
                lines.add(new Line(lineNumber, 0D, true));
            }
        }
        ingresses.add(ingress);
        return lines;
    }
}
