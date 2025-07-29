package ust.tad.kubernetesmpsplugin.analysis.kubernetesparser;

import ust.tad.kubernetesmpsplugin.kubernetesmodel.common.types.StringStringMap;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.configStorageResources.PersistentVolumeClaim;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.workload.deployment.KubernetesDeployment;
import ust.tad.kubernetesmpsplugin.models.tsdm.InvalidAnnotationException;
import ust.tad.kubernetesmpsplugin.models.tsdm.Line;

import java.util.*;

public class DeploymentParser extends BaseParser{
    public static List<Line> parseDeployment(int lineNumber, List<String> readInLines,
                                             Set<KubernetesDeployment> deployments,
                                             Set<PersistentVolumeClaim> pvcs)
            throws InvalidAnnotationException {
        int initialLineNumber = lineNumber;
        List<Line> lines = new ArrayList<>();
        KubernetesDeployment kubernetesDeployment  = new KubernetesDeployment();
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
                            if (currentLine.equals("") || currentLine.trim().startsWith("#")) {
                                lines.add(new Line(lineNumber, 1D, true));
                            } else {
                                String[] lineSplit = currentLine.split(":");
                                StringStringMap label = new StringStringMap(getCleanedValue(lineSplit[0]), getCleanedValue(lineSplit[1]));
                                Set<StringStringMap> labels = kubernetesDeployment.getLabels();
                                labels.add(label);
                                kubernetesDeployment.setLabels(labels);
                                lines.add(new Line(lineNumber, 1D, true));
                            }
                        }
                        if (linesIterator.hasNext()) {
                            linesIterator.previous();
                        }
                    } else if (currentLine.trim().startsWith("name:")) {
                        kubernetesDeployment.setName(getCleanedValue(currentLine.split("name:")[1]));
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
                while (linesIterator.hasNext() && ((currentLine = linesIterator.next()).startsWith("  ") || currentLine.equals("") || currentLine.trim().startsWith("#"))) {
                    lineNumber++;
                    if (currentLine.trim().startsWith("replicas:")) {
                        lines.add(new Line(lineNumber, 1D, true));
                        kubernetesDeployment.setReplicas(Integer.parseInt(getCleanedValue(currentLine.split("replicas:")[1])));
                    } else if (currentLine.trim().startsWith("minReadySeconds:")) {
                        lines.add(new Line(lineNumber, 1D, true));
                        kubernetesDeployment.setMinReadySeconds(Integer.parseInt(getCleanedValue(currentLine.split(":")[1])));
                    } else if (currentLine.trim().startsWith("paused:")) {
                        lines.add(new Line(lineNumber, 1D, true));
                        kubernetesDeployment.setPause(Boolean.parseBoolean(getCleanedValue(currentLine.split(":")[1])));
                    } else if (currentLine.trim().startsWith("revisionHistoryLimit:")) {
                        lines.add(new Line(lineNumber, 1D, true));
                        kubernetesDeployment.setRevisionHistorySeconds(Integer.parseInt(getCleanedValue(currentLine.split(":")[1])));
                    } else if (currentLine.trim().startsWith("selector:")) {
                        lines.add(new Line(lineNumber, 1D, true));
                        while(linesIterator.hasNext() && ((currentLine = linesIterator.next()).startsWith("    ") || currentLine.equals("") || currentLine.trim().startsWith("#"))) {
                            lineNumber++;
                            if (currentLine.trim().startsWith("matchLabels:")) {
                                lines.add(new Line(lineNumber, 1D, true));
                                while(linesIterator.hasNext() && ((currentLine = linesIterator.next()).startsWith("      ") || currentLine.equals("") || currentLine.trim().startsWith("#"))) {
                                    lineNumber++;
                                    if (currentLine.equals("") || currentLine.trim().startsWith("#")) {
                                        lines.add(new Line(lineNumber, 1D, true));
                                    } else {
                                        String[] lineSplit = currentLine.split(":");
                                        StringStringMap selectorMatchLabel = new StringStringMap(getCleanedValue(lineSplit[0]), getCleanedValue(lineSplit[1]));
                                        Set<StringStringMap> selectorMatchLabels = kubernetesDeployment.getSelectorMatchLabels();
                                        selectorMatchLabels.add(selectorMatchLabel);
                                        kubernetesDeployment.setLabels(selectorMatchLabels);
                                        lines.add(new Line(lineNumber, 1D, true));
                                    }
                                }
                                if (linesIterator.hasNext()) {
                                    linesIterator.previous();
                                }
                            } else {
                                lines.add(new Line(lineNumber, 0D, true));
                            }


                            lines.add(new Line(lineNumber, 1D, true));
                        }
                        if (linesIterator.hasNext()) {
                            linesIterator.previous();
                        }
                    } else if (currentLine.trim().startsWith("template:")) {
                        lines.add(new Line(lineNumber, 1D, true));
                        lineNumber++;
                        lines.addAll(PodSpecParser.parsePod(lineNumber, readInLines.subList(
                                        lineNumber-initialLineNumber, readInLines.size()),
                                kubernetesDeployment));
                    } else if (currentLine.trim().startsWith("volumeClaimTemplates:")) {
                        lines.add(new Line(lineNumber, 1D, true));
                        // create pvc with vct-name - deployment name
                        lines.addAll(PersistentVolumeClaimParser.parsePersistentVolumeClaim(
                                lineNumber, readInLines.subList(lineNumber-initialLineNumber,
                                        readInLines.size()), pvcs, Optional.of(kubernetesDeployment)));
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
            } else if (currentLine.equals("") || currentLine.trim().startsWith("#")) {
                lines.add(new Line(lineNumber, 1D, true));
            } else {
                lines.add(new Line(lineNumber, 0D, true));
            }
            lineNumber++;
        }
        deployments.add(kubernetesDeployment);
        return lines;
    }
}
