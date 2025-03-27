package ust.tad.kubernetesmpsplugin.analysis.kubernetesparser;

import ust.tad.kubernetesmpsplugin.kubernetesmodel.common.types.StringStringMap;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.service.KubernetesService;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.service.ServicePort;
import ust.tad.kubernetesmpsplugin.models.tsdm.InvalidAnnotationException;
import ust.tad.kubernetesmpsplugin.models.tsdm.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class ServiceParser extends BaseParser {
    public static List<Line> parseService(int lineNumber, List<String> readInLines, Set<KubernetesService> services) throws InvalidAnnotationException {
        List<Line> lines = new ArrayList<>();
        KubernetesService kubernetesService = new KubernetesService();
        ListIterator<String> linesIterator = readInLines.listIterator();

        while (linesIterator.hasNext()) {
            String currentLine = linesIterator.next();
            if (currentLine.startsWith("metadata:")) {
                lines.add(new Line(lineNumber, 1D, true));
                while (linesIterator.hasNext() && ((currentLine = linesIterator.next()).startsWith("  ") || currentLine.equals("") || currentLine.trim().startsWith("#"))) {
                    lineNumber++;
                    if (currentLine.trim().startsWith("labels:")) {
                        lines.add(new Line(lineNumber, 1D, true));
                        while(linesIterator.hasNext() && ((currentLine = linesIterator.next()).startsWith("    ") || currentLine.equals("") || currentLine.trim().startsWith("#"))) {
                            lineNumber++;
                            lines.add(new Line(lineNumber, 1D, true));
                        }
                        if (linesIterator.hasNext()) {
                            linesIterator.previous();
                        }
                    } else if (currentLine.trim().startsWith("name:")) {
                        kubernetesService.setName(getCleanedValue(currentLine.split("name:")[1]));
                        lines.add(new Line(lineNumber, 1D, true));
                    } else if (currentLine.equals("") || currentLine.trim().startsWith("#")) {
                        lines.add(new Line(lineNumber, 1D, true));
                    } else {
                        lines.add(new Line(lineNumber, 0D, true));
                    }
                }
                if (linesIterator.hasNext()) {
                    linesIterator.previous();
                }
            } else if (currentLine.startsWith("spec:")) {
                lines.add(new Line(lineNumber, 1D, true));
                while (linesIterator.hasNext() && ((currentLine = linesIterator.next()).startsWith("  ") || currentLine.equals("") || currentLine.trim().startsWith("#")))  {
                    lineNumber++;
                    if (currentLine.trim().startsWith("ports:")) {
                        lines.add(new Line(lineNumber, 1D, true));
                        while(linesIterator.hasNext() && ((currentLine = linesIterator.next()).matches("^\\s*-.*") || currentLine.equals("") || currentLine.trim().startsWith("#")))  {
                            if (currentLine.equals("") || currentLine.trim().startsWith("#")) {
                                lines.add(new Line(lineNumber, 1D, true));
                                continue;
                            }
                            ServicePort servicePort = new ServicePort();
                            currentLine = currentLine.replaceFirst("-", " ");
                            int numberOfWhitespaces = currentLine.length() - currentLine.stripLeading().length();
                            String leadingWhiteSpaces = currentLine.substring(0, numberOfWhitespaces);
                            while(currentLine.startsWith(leadingWhiteSpaces) || currentLine.equals("") || currentLine.trim().startsWith("#")) {
                                lineNumber++;
                                String[] lineSplit = currentLine.split(":");
                                if (currentLine.trim().startsWith("name:")) {
                                    lines.add(new Line(lineNumber, 1D, true));
                                    servicePort.setName(getCleanedValue(lineSplit[1]));
                                } else if (currentLine.trim().startsWith("port:")) {
                                    lines.add(new Line(lineNumber, 1D, true));
                                    servicePort.setPort(Integer.parseInt(getCleanedValue(lineSplit[1])));
                                } else if (currentLine.trim().startsWith("targetPort:")) {
                                    lines.add(new Line(lineNumber, 1D, true));
                                    servicePort.setTargetPort(getCleanedValue(lineSplit[1]));
                                } else if (currentLine.equals("") || currentLine.trim().startsWith("#")) {
                                    lines.add(new Line(lineNumber, 1D, true));
                                } else {
                                    lines.add(new Line(lineNumber, 0D, true));
                                }
                                if (linesIterator.hasNext()) {
                                    currentLine = linesIterator.next();
                                } else {
                                    break;
                                }
                            }
                            if (linesIterator.hasNext()) {
                                linesIterator.previous();
                            }
                            Set<ServicePort> servicePorts = kubernetesService.getServicePorts();
                            servicePorts.add(servicePort);
                            kubernetesService.setServicePorts(servicePorts);
                        }
                        if (linesIterator.hasNext()) {
                            linesIterator.previous();
                        }
                    } else if (currentLine.trim().startsWith("selector:")) {
                        lines.add(new Line(lineNumber, 1D, true));
                        while(linesIterator.hasNext() && ((currentLine = linesIterator.next()).startsWith("    ") || currentLine.equals("") || currentLine.trim().startsWith("#")))  {
                            lineNumber++;
                            if (currentLine.equals("") || currentLine.trim().startsWith("#")) {
                                lines.add(new Line(lineNumber, 1D, true));
                            } else {
                                String[] lineSplit = currentLine.split(":");
                                StringStringMap selector = new StringStringMap(getCleanedValue(lineSplit[0]), getCleanedValue(lineSplit[1]));
                                Set<StringStringMap> selectors = kubernetesService.getSelectors();
                                selectors.add(selector);
                                kubernetesService.setSelectors(selectors);
                                lines.add(new Line(lineNumber, 1D, true));
                            }
                        }
                        if (linesIterator.hasNext()) {
                            linesIterator.previous();
                        }
                    } else if (currentLine.trim().startsWith("clusterIP:")) {
                        lines.add(new Line(lineNumber, 1D, true));
                        kubernetesService.setClusterIP(getCleanedValue(currentLine.split(":")[1]));
                    } else if (currentLine.equals("") || currentLine.trim().startsWith("#")) {
                        lines.add(new Line(lineNumber, 1D, true));
                    } else {
                        lines.add(new Line(lineNumber, 0D, true));
                    }
                }
                if (linesIterator.hasNext()) {
                    linesIterator.previous();
                }
            } else if (currentLine.startsWith("kind:")) {
                lines.add(new Line(lineNumber, 1D, true));
            } else {
                lines.add(new Line(lineNumber, 0D, true));
            }
            lineNumber++;
        }
        services.add(kubernetesService);
        return lines;
    }
}
