package ust.tad.kubernetesmpsplugin.analysis.kubernetesparser;

import ust.tad.kubernetesmpsplugin.kubernetesmodel.configStorageResources.Volume;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.workload.deployment.KubernetesDeployment;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.common.types.StringStringMap;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.workload.pods.*;
import ust.tad.kubernetesmpsplugin.models.tsdm.InvalidAnnotationException;
import ust.tad.kubernetesmpsplugin.models.tsdm.Line;

import java.util.*;

public class PodSpecParser extends BaseParser {
    public static List<Line> parsePod(int lineNumber, List<String> readInLines, KubernetesDeployment deployment) throws InvalidAnnotationException {
        List<Line> lines = new ArrayList<>();

        KubernetesPodSpec podSpec = new KubernetesPodSpec();
        Optional<KubernetesPodSpec> existingPodSpec = deployment.getPodSpecs().stream().findFirst();
        if (existingPodSpec.isPresent()) {
            podSpec = existingPodSpec.get();
        } else {
            deployment.getPodSpecs().add(podSpec);
        }

        ListIterator<String> iterator = readInLines.listIterator();
        int indentation = -1;
        while (iterator.hasNext()) {
            String currentLine = iterator.next();
            // in first iteration determine indentation because PodSpec can occur indented, and we need to know this
            // offset to correctly determine the boundaries of an even further indented block
            // i.e. when passing PodSpec from Deployment anything regarded PodSpec is indented by at least 2 spaces
            if (indentation < 0) indentation = countLeadingWhitespaces(currentLine);
            // When called from i.e. deployment, the Pod Template is indented by 2. If currentLine is less indented
            // we know that we finished analyzing everything belonging to the pod specification
            if (countLeadingWhitespaces(currentLine) < indentation && !(currentLine.equals("") || currentLine.trim().startsWith("#"))) break;
            if (currentLine.trim().startsWith("metadata:")) {
                lines.add(new Line(lineNumber, 1D, true));
                while (iterator.hasNext() && (countLeadingWhitespaces(currentLine = iterator.next()) >= indentation + 2 || currentLine.equals("") || currentLine.trim().startsWith("#"))) {
                    lineNumber++;
                    if (currentLine.trim().startsWith("labels:")) {
                        lines.add(new Line(lineNumber, 1D, true));
                        while (iterator.hasNext() && (countLeadingWhitespaces(currentLine = iterator.next()) >= indentation + 4 || currentLine.equals("") || currentLine.trim().startsWith("#"))) {
                            lineNumber++;
                            if (currentLine.equals("") || currentLine.trim().startsWith("#")) {
                                lines.add(new Line(lineNumber, 1D, true));
                            } else {
                                String[] lineSplit = currentLine.split(":");
                                StringStringMap label = new StringStringMap(getCleanedValue(lineSplit[0]), getCleanedValue(lineSplit[1]));
                                Set<StringStringMap> labels = podSpec.getLabels();
                                labels.add(label);
                                podSpec.setLabels(labels);
                                lines.add(new Line(lineNumber, 1D, true));
                            }
                        }
                        if (iterator.hasNext()) iterator.previous();
                    } else if (currentLine.equals("") || currentLine.trim().startsWith("#")) {
                        lines.add(new Line(lineNumber, 1D, true));
                    } else {
                        lines.add(new Line(lineNumber, 0D, true));
                    }
                }
                if (iterator.hasNext()) iterator.previous();
            } else if (currentLine.trim().startsWith("spec:")) {
                lines.add(new Line(lineNumber, 1D, true));
                while (iterator.hasNext() && (countLeadingWhitespaces(currentLine = iterator.next()) >= indentation + 2 || currentLine.equals("") || currentLine.trim().startsWith("#"))) {
                    lineNumber++;
                    if (currentLine.trim().startsWith("containers:")) {
                        lines.add(new Line(lineNumber, 1D, true));
                        int containerLeadingSpaces = countLeadingWhitespaces(currentLine);
                        while (iterator.hasNext() && (((currentLine = iterator.next()).trim().startsWith("-") && countLeadingWhitespaces(currentLine) >= containerLeadingSpaces) || currentLine.equals("") || currentLine.trim().startsWith("#"))) {
                            lineNumber++;
                            Container container = new Container();
                            currentLine = currentLine.replaceFirst("-", " ");
                            while (countLeadingWhitespaces(currentLine) > containerLeadingSpaces || currentLine.equals("") || currentLine.trim().startsWith("#")) {
                                if (currentLine.trim().startsWith("name:")) {
                                    lines.add(new Line(lineNumber, 1D, true));
                                    container.setName(getCleanedValue(currentLine.split(":")[1]));
                                } else if (currentLine.trim().startsWith("image:")) {
                                    lines.add(new Line(lineNumber, 1D, true));
                                    container.setImage(getCleanedValue(currentLine.split("image:")[1]));
                                } else if (currentLine.trim().startsWith("imagePullPolicy:")) {
                                    lines.add(new Line(lineNumber, 1D, true));
                                    container.setImagePullPolicy(getCleanedValue(currentLine.split(":")[1]));
                                } else if (currentLine.trim().startsWith("ports:")) {
                                    int portsLeadingWhitespaces = countLeadingWhitespaces(currentLine);
                                    lines.add(new Line(lineNumber, 1D, true));
                                    Set<ContainerPort> containerPorts = new HashSet<>();
                                    while (iterator.hasNext() && (((currentLine = iterator.next()).trim().startsWith("-") && countLeadingWhitespaces(currentLine) >= portsLeadingWhitespaces) || currentLine.equals("") || currentLine.trim().startsWith("#"))) {
                                        ContainerPort containerPort = new ContainerPort();
                                        currentLine = currentLine.replaceFirst("-", " ");
                                        while (countLeadingWhitespaces(currentLine) > portsLeadingWhitespaces || currentLine.equals("") || currentLine.trim().startsWith("#")) {
                                            if (currentLine.trim().startsWith("-")) {
                                                break;
                                            }
                                            lineNumber++;
                                            String[] lineSplitPort = currentLine.split(":");
                                            if (currentLine.trim().startsWith("name:")) {
                                                lines.add(new Line(lineNumber, 1D, true));
                                                containerPort.setName(getCleanedValue(lineSplitPort[1]));
                                            } else if (currentLine.trim().startsWith("containerPort:")) {
                                                lines.add(new Line(lineNumber, 1D, true));
                                                containerPort.setPort(Integer.parseInt(getCleanedValue(lineSplitPort[1])));
                                            } else if (currentLine.equals("") || currentLine.trim().startsWith("#")) {
                                                lines.add(new Line(lineNumber, 1D, true));
                                            } else {
                                                lines.add(new Line(lineNumber, 0D, true));
                                            }
                                            if (iterator.hasNext()) {
                                                currentLine = iterator.next();
                                            } else {
                                                break;
                                            }
                                        }
                                        if (iterator.hasNext()) {
                                            iterator.previous();
                                        }
                                        containerPorts.add(containerPort);
                                    }
                                    if (iterator.hasNext()) {
                                        iterator.previous();
                                    }
                                    container.setContainerPorts(containerPorts);
                                } else if (currentLine.trim().startsWith("env:")) {
                                    int envLeadingWhitespaces = countLeadingWhitespaces(currentLine);
                                    lines.add(new Line(lineNumber, 1D, true));
                                    Set<EnvironmentVariable> environmentVariables = new HashSet<>();
                                    while (iterator.hasNext() && (((currentLine = iterator.next()).trim().startsWith("-") && countLeadingWhitespaces(currentLine) >= envLeadingWhitespaces) || currentLine.equals("") || currentLine.trim().startsWith("#"))) {
                                        EnvironmentVariable environmentVariable = new EnvironmentVariable();
                                        currentLine = currentLine.replaceFirst("-", " ");
                                        while (countLeadingWhitespaces(currentLine) > envLeadingWhitespaces || currentLine.equals("") || currentLine.trim().startsWith("#")) {
                                            if (currentLine.trim().startsWith("-")) {
                                                break;
                                            }
                                            lineNumber++;
                                            if (currentLine.trim().startsWith("name:")) {
                                                lines.add(new Line(lineNumber, 1D, true));
                                                environmentVariable.setKey(getCleanedValue(currentLine.split("name:")[1]));
                                            } else if (currentLine.trim().startsWith("value:")) {
                                                lines.add(new Line(lineNumber, 1D, true));
                                                environmentVariable.setValue(getCleanedValue(currentLine.split("value:")[1]));
                                            } else if (currentLine.equals("") || currentLine.trim().startsWith("#")) {
                                                lines.add(new Line(lineNumber, 1D, true));
                                            } else {
                                                lines.add(new Line(lineNumber, 0D, true));
                                            }
                                            if (iterator.hasNext()) {
                                                currentLine = iterator.next();
                                            } else {
                                                break;
                                            }
                                        }
                                        if (iterator.hasNext()) iterator.previous();
                                        if (environmentVariable.getKey() != null && environmentVariable.getValue() != null) {
                                            environmentVariables.add(environmentVariable);
                                        }
                                    }
                                    if (iterator.hasNext()) iterator.previous();
                                    container.setEnvironmentVariables(environmentVariables);
                                } else if (currentLine.trim().startsWith("workingDir")) {
                                    lines.add(new Line(lineNumber, 1D, true));
                                    container.setWorkingDir(getCleanedValue(currentLine.split("workingDir:")[1]));
                                } else if (currentLine.trim().startsWith("args:")) {
                                    lines.add(new Line(lineNumber, 1D, true));
                                    while (iterator.hasNext() && ((currentLine = iterator.next()).trim().startsWith("-") || currentLine.equals("") || currentLine.trim().startsWith("#"))) {
                                        lineNumber++;
                                        lines.add(new Line(lineNumber, 1D, true));
                                        if (!(currentLine.equals("") || currentLine.trim().startsWith("#"))) {
                                            container.getArgs().add(currentLine.trim().replaceFirst("-", ""));
                                        }
                                    }
                                    if (iterator.hasNext()) iterator.previous();
                                } else if (currentLine.trim().startsWith("command:")) {
                                    lines.add(new Line(lineNumber, 1D, true));
                                    while (iterator.hasNext() && ((currentLine = iterator.next()).trim().startsWith("-") || currentLine.equals("") || currentLine.trim().startsWith("#"))) {
                                        lineNumber++;
                                        lines.add(new Line(lineNumber, 1D, true));
                                        if (!(currentLine.equals("") || currentLine.trim().startsWith("#"))) {
                                            container.getCommand().add(currentLine.trim().replaceFirst("-", ""));
                                        }
                                    }
                                    if (iterator.hasNext()) iterator.previous();
                                } else if (currentLine.trim().startsWith("volumeMounts:")) {
                                    int volumeMountsLeadingWhitespaces = countLeadingWhitespaces(currentLine);
                                    lines.add(new Line(lineNumber, 1D, true));
                                    while (iterator.hasNext() && (((currentLine = iterator.next()).trim().startsWith("-") && countLeadingWhitespaces(currentLine) >= volumeMountsLeadingWhitespaces) || currentLine.equals("") || currentLine.trim().startsWith("#"))) {
                                        VolumeMount volumeMount = new VolumeMount();
                                        currentLine = currentLine.replaceFirst("-", " ");
                                        while (countLeadingWhitespaces(currentLine) > volumeMountsLeadingWhitespaces || currentLine.equals("") || currentLine.trim().startsWith("#")) {
                                            if (currentLine.trim().startsWith("-")) {
                                                break;
                                            }
                                            lineNumber++;
                                            if (currentLine.trim().startsWith("mountPath:")) {
                                                lines.add(new Line(lineNumber, 1D, true));
                                                volumeMount.setMountPath(getCleanedValue(currentLine.trim().split(":")[1]));
                                            } else if (currentLine.trim().startsWith("name:")) {
                                                lines.add(new Line(lineNumber, 1D, true));
                                                volumeMount.setName(getCleanedValue(currentLine.trim().split(":")[1]));
                                            } else if (currentLine.trim().startsWith("readOnly:")) {
                                                lines.add(new Line(lineNumber, 1D, true));
                                                volumeMount.setReadOnly(Boolean.parseBoolean(getCleanedValue(currentLine.trim().split(":")[1])));
                                            } else if (currentLine.trim().startsWith("subPath:")) {
                                                lines.add(new Line(lineNumber, 1D, true));
                                                String[] currentLineSplit = currentLine.trim().split(":");
                                                if (currentLineSplit.length > 1) {
                                                    volumeMount.setSubPath(getCleanedValue(currentLineSplit[1]));
                                                }
                                            } else if (currentLine.trim().startsWith("subPathExpr:")) {
                                                lines.add(new Line(lineNumber, 1D, true));
                                                String[] currentLineSplit = currentLine.trim().split(":");
                                                if (currentLineSplit.length > 1) {
                                                    volumeMount.setSubPathExpr(getCleanedValue(currentLineSplit[1]));
                                                }
                                            } else if (currentLine.trim().startsWith("mountPropagation:")) {
                                                lines.add(new Line(lineNumber, 1D, true));
                                                String[] currentLineSplit = currentLine.trim().split(":");
                                                if (currentLineSplit.length > 1) {
                                                    volumeMount.setMountPropagation(getCleanedValue(currentLineSplit[1]));
                                                }
                                            } else if (currentLine.equals("") || currentLine.trim().startsWith("#")) {
                                                lines.add(new Line(lineNumber, 1D, true));
                                            } else {
                                                lines.add(new Line(lineNumber, 0D, true));
                                            }
                                            if (iterator.hasNext()) {
                                                currentLine = iterator.next();
                                            } else {
                                                break;
                                            }
                                        }
                                        if (iterator.hasNext()) iterator.previous();
                                        container.getVolumeMounts().add(volumeMount);
                                    }
                                    if (iterator.hasNext()) iterator.previous();
                                } else if (currentLine.equals("") || currentLine.trim().startsWith("#")) {
                                    lines.add(new Line(lineNumber, 1D, true));
                                } else {
                                    lines.add(new Line(lineNumber, 0D, true));
                                }
                                if (iterator.hasNext()) {
                                    currentLine = iterator.next();
                                    lineNumber++;
                                } else {
                                    break;
                                }
                            }
                            if (iterator.hasNext()) {
                                iterator.previous();
                            }
                            Set<Container> containers = podSpec.getContainers();
                            containers.add(container);
                            podSpec.setContainers(containers);
                        }
                        if (iterator.hasNext()) {
                            iterator.previous();
                        }
                    } else if (currentLine.trim().startsWith("restartPolicy")) {
                        lines.add(new Line(lineNumber, 1D, true));
                        podSpec.setRestartPolicy(getCleanedValue(currentLine.split(":")[1]));
                    } else if (currentLine.trim().startsWith("volumes")) {
                        int volumesLeadingWhitespaces = countLeadingWhitespaces(currentLine);
                        lines.add(new Line(lineNumber, 1D, true));
                        while (iterator.hasNext() &&
                                (((currentLine = iterator.next()).trim().startsWith("-") &&
                                countLeadingWhitespaces(currentLine) >= volumesLeadingWhitespaces) || currentLine.equals("") || currentLine.trim().startsWith("#"))) {
                            Volume volume = new Volume();
                            currentLine = currentLine.replaceFirst("-", " ");
                            while (countLeadingWhitespaces(currentLine) > volumesLeadingWhitespaces || currentLine.equals("") || currentLine.trim().startsWith("#")) {
                                if (currentLine.trim().startsWith("-")) {
                                    break;
                                }
                                lineNumber++;
                                if (currentLine.trim().startsWith("name:")) {
                                    lines.add(new Line(lineNumber, 1D, true));
                                    volume.setName(getCleanedValue(currentLine.trim().split(":")[1]));
                                } else if (currentLine.trim().startsWith("persistentVolumeClaim:")) {
                                    lines.add(new Line(lineNumber, 1D, true));
                                    while (iterator.hasNext() &&
                                            (countLeadingWhitespaces((currentLine = iterator.next())) > volumesLeadingWhitespaces || currentLine.equals("") || currentLine.trim().startsWith("#"))) {
                                        if (currentLine.trim().startsWith("-")) {
                                            break;
                                        }
                                        lineNumber++;
                                        if (currentLine.trim().startsWith("claimName:")) {
                                            volume.setPersistentVolumeClaimName(getCleanedValue(currentLine.trim().split(":")[1]));
                                            lines.add(new Line(lineNumber, 1D, true));
                                        } else if (currentLine.trim().startsWith("readOnly:")) {
                                            lines.add(new Line(lineNumber, 1D, true));
                                            volume.setPersistentVolumeClaimReadOnly(Boolean.parseBoolean(
                                                    getCleanedValue(currentLine.trim().split(":")[1])));
                                        } else if (currentLine.equals("") || currentLine.trim().startsWith("#")) {
                                            lines.add(new Line(lineNumber, 1D, true));
                                        } else {
                                            lines.add(new Line(lineNumber, 0D, true));
                                        }
                                        if (iterator.hasNext()) iterator.previous();
                                    }
                                } else if (currentLine.equals("") || currentLine.trim().startsWith("#")) {
                                    lines.add(new Line(lineNumber, 1D, true));
                                } else {
                                    lines.add(new Line(lineNumber, 0D, true));
                                }
                                if (iterator.hasNext()) {
                                    currentLine = iterator.next();
                                } else {
                                    break;
                                }
                            }
                            podSpec.getVolumes().add(volume);
                            if (iterator.hasNext()) iterator.previous();
                        }
                        if (iterator.hasNext()) iterator.previous();
                    } else if (currentLine.equals("") || currentLine.trim().startsWith("#")) {
                        lines.add(new Line(lineNumber, 1D, true));
                    } else {
                        lines.add(new Line(lineNumber, 0D, true));
                    }
                }
                if (iterator.hasNext()) {
                    iterator.previous();
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
        return lines;
    }
}
