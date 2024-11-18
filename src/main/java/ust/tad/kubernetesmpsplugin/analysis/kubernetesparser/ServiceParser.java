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
                while (linesIterator.hasNext() && (currentLine = linesIterator.next()).startsWith("  ")) {
                    lineNumber++;
                    if (currentLine.trim().startsWith("labels:")) {
                        lines.add(new Line(lineNumber, 1D, true));
                        while(linesIterator.hasNext() && linesIterator.next().startsWith("    ")) {
                            lineNumber++;
                            lines.add(new Line(lineNumber, 1D, true));
                        }
                        if (linesIterator.hasNext()) {
                            linesIterator.previous();
                        }
                    } else if (currentLine.trim().startsWith("name:")) {
                        String name = currentLine.split("name:")[1].trim();
                        kubernetesService.setName(name);
                        lines.add(new Line(lineNumber, 1D, true));
                    } else if (currentLine.trim().startsWith("#")) {
                        continue;
                    }
                    else {
                        lines.add(new Line(lineNumber, 0D, true));
                    }
                }
                if (linesIterator.hasNext()) {
                    linesIterator.previous();
                }
            } else if (currentLine.startsWith("spec:")) {
                lines.add(new Line(lineNumber, 1D, true));
                while (linesIterator.hasNext() && (currentLine = linesIterator.next()).startsWith("  ")) {
                    lineNumber++;
                    if (currentLine.trim().startsWith("ports:")) {
                        lines.add(new Line(lineNumber, 1D, true));
                        while(linesIterator.hasNext() && (currentLine = linesIterator.next()).matches("^\\s*-.*")) {
                            ServicePort servicePort = new ServicePort();
                            currentLine = currentLine.replaceFirst("-", " ");
                            int numberOfWhitespaces = currentLine.length() - currentLine.stripLeading().length();
                            String leadingWhiteSpaces = currentLine.substring(0, numberOfWhitespaces);
                            while(currentLine.startsWith(leadingWhiteSpaces)) {
                                lineNumber++;
                                String[] lineSplit = currentLine.split(":");
                                if (currentLine.trim().startsWith("name:")) {
                                    lines.add(new Line(lineNumber, 1D, true));
                                    servicePort.setName(lineSplit[1].trim());
                                } else if (currentLine.trim().startsWith("port:")) {
                                    lines.add(new Line(lineNumber, 1D, true));
                                    servicePort.setPort(Integer.parseInt(lineSplit[1].trim()));
                                } else if (currentLine.trim().startsWith("targetPort:")) {
                                    lines.add(new Line(lineNumber, 1D, true));
                                    servicePort.setTargetPort(lineSplit[1].trim());
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
                        while(linesIterator.hasNext() && (currentLine = linesIterator.next()).startsWith("    ")) {
                            lineNumber++;
                            String[] lineSplit = currentLine.split(":");
                            StringStringMap selector = new StringStringMap(lineSplit[0].trim(), lineSplit[1].trim());
                            Set<StringStringMap> selectors = kubernetesService.getSelectors();
                            selectors.add(selector);
                            kubernetesService.setSelectors(selectors);
                            lines.add(new Line(lineNumber, 1D, true));
                        }
                        if (linesIterator.hasNext()) {
                            linesIterator.previous();
                        }
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
