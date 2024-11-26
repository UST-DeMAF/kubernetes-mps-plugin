package ust.tad.kubernetesmpsplugin.analysis.kubernetesparser;

import ust.tad.kubernetesmpsplugin.kubernetesmodel.configStorageResources.PersistentVolumeClaim;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.configStorageResources.Volume;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.workload.deployment.KubernetesDeployment;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.workload.pods.KubernetesPodSpec;
import ust.tad.kubernetesmpsplugin.models.tsdm.InvalidAnnotationException;
import ust.tad.kubernetesmpsplugin.models.tsdm.Line;

import java.util.*;

public class PersistentVolumeClaimParser extends BaseParser {
    public static List<Line> parsePersistentVolumeClaim(int lineNumber, List<String> readInLines, Set<PersistentVolumeClaim> pvcs, Optional<KubernetesDeployment> deployment) throws InvalidAnnotationException {
        List<Line> lines = new ArrayList<>();
        PersistentVolumeClaim pvc = new PersistentVolumeClaim();
        ListIterator<String> iterator = readInLines.listIterator();
        while (iterator.hasNext()) {
            String currentLine = iterator.next();
            currentLine = currentLine.replaceFirst("-", " ");
            int leadingWhiteSpaces = countLeadingWhitespaces(currentLine);
            if (currentLine.trim().startsWith("metadata:")) {
                lines.add(new Line(lineNumber, 1D, true));
                while (iterator.hasNext() && (countLeadingWhitespaces(currentLine = iterator.next())) > leadingWhiteSpaces) {
                    lineNumber++;
                    if (currentLine.trim().startsWith("name:")) {
                        String name = currentLine.split("name:")[1].trim();
                        pvc.setName(name);
                        lines.add(new Line(lineNumber, 1D, true));
                    } else if (currentLine.trim().startsWith("#")) {
                        continue;
                    }
                    else {
                        lines.add(new Line(lineNumber, 0D, true));
                    }
                }
                if (iterator.hasNext()) {
                    iterator.previous();
                }
            } else if (currentLine.trim().startsWith("spec:")) {
                lineNumber++;
                lines.add(new Line(lineNumber, 1D, true));
                while (iterator.hasNext() && (countLeadingWhitespaces(currentLine = iterator.next())) > leadingWhiteSpaces) {
                    lineNumber++;
                    if (currentLine.trim().startsWith("volumeName:")) {
                        lines.add(new Line(lineNumber, 1D, true));
                        pvc.setVolumeName(currentLine.split(":")[1].trim());
                    } else if (currentLine.trim().startsWith("resources:")) {
                        lines.add(new Line(lineNumber, 1D, true));
                        int resourcesLeadingWhitespaces = countLeadingWhitespaces(currentLine);
                        while (iterator.hasNext() && (countLeadingWhitespaces(currentLine = iterator.next())) > resourcesLeadingWhitespaces) {
                            lineNumber++;
                            if (currentLine.trim().startsWith("limits:")) {
                                lines.add(new Line(lineNumber, 1D, true));
                                currentLine = iterator.next();
                                lines.add(new Line(lineNumber, 1D, true));
                                if (currentLine.trim().startsWith("storage:")) {
                                    pvc.setLimit(currentLine.split(":")[1].trim());
                                } else {
                                    lines.add(new Line(lineNumber, 0D, true));
                                }
                            } else if (currentLine.trim().startsWith("requests:")) {
                                lines.add(new Line(lineNumber, 1D, true));
                                currentLine = iterator.next();
                                lines.add(new Line(lineNumber, 1D, true));
                                if (currentLine.trim().startsWith("storage:")) {
                                    pvc.setRequests(currentLine.split(":")[1].trim());
                                } else {
                                    lines.add(new Line(lineNumber, 0D, true));
                                }
                            } else {
                                lines.add(new Line(lineNumber, 0D, true));
                            }
                        }
                    } else {
                        lines.add(new Line(lineNumber, 0D, true));
                    }
                }

            } else {
                lines.add(new Line(lineNumber, 0D, true));
            }
        }

        if (deployment.isPresent()) {
            Volume volume = new Volume();
            volume.setName(pvc.getName());
            String newPVCName = pvc.getName().concat("-").concat(deployment.get().getName());
            pvc.setName(newPVCName);
            volume.setPersistentVolumeClaimName(newPVCName);
            Optional<KubernetesPodSpec> pod = deployment.get().getPodSpecs().stream().findFirst();
            pod.ifPresent(kubernetesPodSpec -> kubernetesPodSpec.getVolumes().add(volume));
        }
        pvcs.add(pvc);
        return lines;
    }
}
