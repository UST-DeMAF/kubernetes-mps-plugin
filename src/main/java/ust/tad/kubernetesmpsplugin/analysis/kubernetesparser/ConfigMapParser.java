package ust.tad.kubernetesmpsplugin.analysis.kubernetesparser;

import ust.tad.kubernetesmpsplugin.kubernetesmodel.common.types.StringStringMap;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.configStorageResources.ConfigMap;
import ust.tad.kubernetesmpsplugin.models.tsdm.InvalidAnnotationException;
import ust.tad.kubernetesmpsplugin.models.tsdm.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class ConfigMapParser extends BaseParser {
    public static List<Line> parseConfigMap(int lineNumber, List<String> readInLines, Set<ConfigMap> configMaps) throws InvalidAnnotationException {
        List<Line> lines = new ArrayList<>();
        ConfigMap configMap = new ConfigMap();
        ListIterator<String> iterator = readInLines.listIterator();

        while (iterator.hasNext()) {
            String currentLine = iterator.next();
            if (currentLine.trim().startsWith("data:")) {
                lineNumber++;
                lines.add(new Line(lineNumber, 1D, true));

                currentLine = iterator.next();
                configMap.setData(new StringStringMap(currentLine.split(":")[0], currentLine.split(":")[1]));

            } else if (currentLine.trim().startsWith("immutable:")) {
                lineNumber++;
                lines.add(new Line(lineNumber, 1D, true));
                configMap.setImmutable(Boolean.parseBoolean(currentLine.split(":")[1]));

            } else if (currentLine.trim().startsWith("metadata:")) {
                lineNumber++;
                lines.add(new Line(lineNumber, 1D, true));

                while (iterator.hasNext() && (currentLine = iterator.next()).startsWith("  ")) {
                    lineNumber++;
                    if (currentLine.trim().startsWith("name:")) {
                        lines.add(new Line(lineNumber, 1D, true));
                        configMap.name(currentLine.split(":")[1]);
                    } else {
                        lines.add(new Line(lineNumber, 0D, true));
                    }
                }

            } else {
                lines.add(new Line(lineNumber, 0D, true));
            }
        }
        configMaps.add(configMap);
        return lines;
    }
}
