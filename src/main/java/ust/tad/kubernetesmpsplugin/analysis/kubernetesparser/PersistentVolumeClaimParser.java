package ust.tad.kubernetesmpsplugin.analysis.kubernetesparser;

import ust.tad.kubernetesmpsplugin.kubernetesmodel.configStorageResources.PersistentVolumeClaim;
import ust.tad.kubernetesmpsplugin.models.tsdm.InvalidAnnotationException;
import ust.tad.kubernetesmpsplugin.models.tsdm.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class PersistentVolumeClaimParser extends BaseParser {
    public static List<Line> parsePersistentVolumeClaim(int lineNumber, List<String> readInLines, Set<PersistentVolumeClaim> pvcs) throws InvalidAnnotationException {
        List<Line> lines = new ArrayList<>();
        PersistentVolumeClaim pvc = new PersistentVolumeClaim();
        ListIterator<String> iterator = readInLines.listIterator();

        while (iterator.hasNext()) {
            String currentLine = iterator.next();
            if (currentLine.trim().startsWith("spec:")) {
                lineNumber++;
                lines.add(new Line(lineNumber, 1D, true));
                while (iterator.hasNext() && (currentLine = iterator.next()).startsWith("  ")) {
                    lineNumber++;
                    if (currentLine.trim().startsWith("volumeName:")) {
                        lines.add(new Line(lineNumber, 1D, true));
                        pvc.setVolumeName(currentLine.split(":")[1].trim());
                    } else if (currentLine.trim().startsWith("resources:")) {
                        lines.add(new Line(lineNumber, 1D, true));
                        while (iterator.hasNext() && (currentLine = iterator.next()).startsWith("    ")) {
                            lineNumber++;
                            if (currentLine.trim().startsWith("limits:")) {
                                lines.add(new Line(lineNumber, 1D, true));
                                currentLine = iterator.next();
                                lines.add(new Line(lineNumber, 1D, true));
                                if (currentLine.trim().startsWith("storage:")) {
                                    pvc.setRequests(currentLine.split(":")[1].trim());
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
        pvcs.add(pvc);
        return lines;
    }
}
