package ust.tad.kubernetesmpsplugin.analysis.kubernetesparser;

import ust.tad.kubernetesmpsplugin.kubernetesmodel.ingress.HTTPIngressRuleValue;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.ingress.IngressBackend;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.ingress.IngressRule;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.ingress.KubernetesIngress;
import ust.tad.kubernetesmpsplugin.models.tsdm.InvalidAnnotationException;
import ust.tad.kubernetesmpsplugin.models.tsdm.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class IngressParser extends BaseParser {

    /**
     * Parse a Kubernetes Ingress object.
     * Nested blocks and list items are identified by their indentation relative to their parent key.
     * Thus, list items may be indented at the same level as their parent key or further.
     *
     * @param lineNumber the line number of the first line of the Ingress object.
     * @param readInLines the lines of the Ingress object, starting with the "kind" line.
     * @param ingresses the set to which the parsed Ingress is added.
     * @return the lines of the Ingress object annotated with their comprehensibility.
     * @throws InvalidAnnotationException if a line annotation is invalid.
     */
    public static List<Line> parseIngress(int lineNumber, List<String> readInLines, Set<KubernetesIngress> ingresses)
            throws InvalidAnnotationException {
        List<String> ingressLines = new ArrayList<>(readInLines);
        double[] comprehensibility = new double[ingressLines.size()];
        KubernetesIngress ingress = new KubernetesIngress();

        for (int index : getEntries(ingressLines, 0, ingressLines.size())) {
            String key = getKey(ingressLines.get(index));
            if (key.equals("kind")) {
                comprehensibility[index] = 1D;
            } else if (key.equals("spec")) {
                comprehensibility[index] = 1D;
                parseSpec(ingressLines, index, ingress, comprehensibility);
            }
        }
        ingresses.add(ingress);

        List<Line> lines = new ArrayList<>();
        for (int index = 0; index < ingressLines.size(); index++) {
            if (isBlankOrComment(ingressLines.get(index))) {
                comprehensibility[index] = 1D;
            }
            lines.add(new Line(lineNumber + index, comprehensibility[index], true));
        }
        return lines;
    }

    private static void parseSpec(List<String> lines, int specIndex, KubernetesIngress ingress,
                                  double[] comprehensibility) {
        for (int index : getEntries(lines, specIndex + 1, getEndOfBlock(lines, specIndex))) {
            String key = getKey(lines.get(index));
            if (key.equals("ingressClassName")) {
                comprehensibility[index] = 1D;
                ingress.setIngressClassName(getValue(lines.get(index)));
            } else if (key.equals("rules")) {
                comprehensibility[index] = 1D;
                for (int ruleIndex : getListItems(lines, index)) {
                    ingress.getRules().add(parseRule(lines, ruleIndex, comprehensibility));
                }
            }
        }
    }

    private static IngressRule parseRule(List<String> lines, int ruleIndex, double[] comprehensibility) {
        IngressRule rule = new IngressRule();
        for (int index : getListItemEntries(lines, ruleIndex)) {
            String key = getKey(lines.get(index));
            if (key.equals("host")) {
                comprehensibility[index] = 1D;
                rule.setHost(getValue(lines.get(index)));
            } else if (key.equals("http")) {
                comprehensibility[index] = 1D;
                for (int httpIndex : getEntries(lines, index + 1, getEndOfBlock(lines, index))) {
                    if (getKey(lines.get(httpIndex)).equals("paths")) {
                        comprehensibility[httpIndex] = 1D;
                        for (int pathIndex : getListItems(lines, httpIndex)) {
                            rule.getIngressRuleValues().add(parsePath(lines, pathIndex, comprehensibility));
                        }
                    }
                }
            }
        }
        return rule;
    }

    private static HTTPIngressRuleValue parsePath(List<String> lines, int pathIndex, double[] comprehensibility) {
        HTTPIngressRuleValue path = new HTTPIngressRuleValue();
        for (int index : getListItemEntries(lines, pathIndex)) {
            String key = getKey(lines.get(index));
            if (key.equals("path")) {
                comprehensibility[index] = 1D;
                path.setPath(getValue(lines.get(index)));
            } else if (key.equals("pathType")) {
                comprehensibility[index] = 1D;
                path.setPathType(getValue(lines.get(index)));
            } else if (key.equals("backend")) {
                comprehensibility[index] = 1D;
                path.setIngressBackend(parseBackend(lines, index, comprehensibility));
            }
        }
        return path;
    }

    /**
     * Parse the backend of an HTTP path.
     * Supports the service backend of networking.k8s.io/v1 (service.name, service.port.name or
     * service.port.number) as well as of networking.k8s.io/v1beta1 (serviceName, servicePort).
     */
    private static IngressBackend parseBackend(List<String> lines, int backendIndex, double[] comprehensibility) {
        IngressBackend backend = new IngressBackend();
        for (int index : getEntries(lines, backendIndex + 1, getEndOfBlock(lines, backendIndex))) {
            String key = getKey(lines.get(index));
            if (key.equals("service")) {
                comprehensibility[index] = 1D;
                for (int serviceIndex : getEntries(lines, index + 1, getEndOfBlock(lines, index))) {
                    String serviceKey = getKey(lines.get(serviceIndex));
                    if (serviceKey.equals("name")) {
                        comprehensibility[serviceIndex] = 1D;
                        backend.setServiceName(getValue(lines.get(serviceIndex)));
                    } else if (serviceKey.equals("port")) {
                        comprehensibility[serviceIndex] = 1D;
                        for (int portIndex : getEntries(lines, serviceIndex + 1, getEndOfBlock(lines, serviceIndex))) {
                            String portKey = getKey(lines.get(portIndex));
                            if (portKey.equals("name") || portKey.equals("number")) {
                                comprehensibility[portIndex] = 1D;
                                setServicePort(backend, getValue(lines.get(portIndex)));
                            }
                        }
                    }
                }
            } else if (key.equals("serviceName")) {
                comprehensibility[index] = 1D;
                backend.setServiceName(getValue(lines.get(index)));
            } else if (key.equals("servicePort")) {
                comprehensibility[index] = 1D;
                setServicePort(backend, getValue(lines.get(index)));
            }
        }
        return backend;
    }

    /**
     * Set the service port of a backend, either as port number or as port name.
     */
    private static void setServicePort(IngressBackend backend, String port) {
        if (port.matches("\\d+")) {
            backend.setServicePortPort(Integer.parseInt(port));
        } else {
            backend.setServicePortName(port);
        }
    }

    /**
     * Get the indices of the items of the list that is the value of the given key.
     */
    private static List<Integer> getListItems(List<String> lines, int keyIndex) {
        List<Integer> items = new ArrayList<>();
        for (int index : getEntries(lines, keyIndex + 1, getEndOfBlock(lines, keyIndex))) {
            if (lines.get(index).trim().startsWith("-")) {
                items.add(index);
            }
        }
        return items;
    }

    /**
     * Get the indices of the entries of a list item.
     * The leading "-" of the list item is replaced by a whitespace, so that the first entry has the same
     * indentation as the following entries of the list item.
     */
    private static List<Integer> getListItemEntries(List<String> lines, int itemIndex) {
        int itemIndentation = countLeadingWhitespaces(lines.get(itemIndex));
        int end = itemIndex + 1;
        while (end < lines.size()
                && (isBlankOrComment(lines.get(end)) || countLeadingWhitespaces(lines.get(end)) > itemIndentation)) {
            end++;
        }
        lines.set(itemIndex, lines.get(itemIndex).replaceFirst("-", " "));
        return getEntries(lines, itemIndex, end);
    }

    /**
     * Get the index after the last line of the value of the given key.
     * The value comprises all following lines that are indented further than the key, as well as list items
     * that are indented at the same level as the key.
     */
    private static int getEndOfBlock(List<String> lines, int keyIndex) {
        int keyIndentation = countLeadingWhitespaces(lines.get(keyIndex));
        for (int index = keyIndex + 1; index < lines.size(); index++) {
            String currentLine = lines.get(index);
            if (isBlankOrComment(currentLine)) {
                continue;
            }
            int indentation = countLeadingWhitespaces(currentLine);
            if (indentation < keyIndentation
                    || (indentation == keyIndentation && !currentLine.trim().startsWith("-"))) {
                return index;
            }
        }
        return lines.size();
    }

    /**
     * Get the indices of the entries between start (inclusive) and end (exclusive), i.e., the lines with the
     * lowest indentation that are neither blank nor comments.
     */
    private static List<Integer> getEntries(List<String> lines, int start, int end) {
        int entryIndentation = Integer.MAX_VALUE;
        for (int index = start; index < end; index++) {
            if (!isBlankOrComment(lines.get(index))) {
                entryIndentation = Math.min(entryIndentation, countLeadingWhitespaces(lines.get(index)));
            }
        }
        List<Integer> entries = new ArrayList<>();
        for (int index = start; index < end; index++) {
            if (!isBlankOrComment(lines.get(index))
                    && countLeadingWhitespaces(lines.get(index)) == entryIndentation) {
                entries.add(index);
            }
        }
        return entries;
    }

    private static String getKey(String line) {
        return line.trim().split(":")[0].trim();
    }

    private static String getValue(String line) {
        return getCleanedValue(line.substring(line.indexOf(':') + 1));
    }

    private static boolean isBlankOrComment(String line) {
        return line.trim().isEmpty() || line.trim().startsWith("#");
    }
}
