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
        ListIterator<String> iterator = readInLines.listIterator();
        int indentation = -1;

        while (iterator.hasNext()) {
            String currentLine = iterator.next();
            // in first iteration determine indentation because PodSpec can occur indented, and we need to know this
            // offset to correctly determine the boundaries of an even further indented block
            // i.e. when passing PodSpec from Deployment anything regarded PodSpec is indented by at least 2 spaces
            if (indentation < 0) indentation = countLeadingWhitespaces(currentLine);

            // When called from i.e. deployment, the Pod Template is  indented by 2. If currentLine is less indented
            // we know that we finished analyzing everything belonging to the pod specification
            if (countLeadingWhitespaces(currentLine) < indentation) break;
            if (currentLine.trim().startsWith("metadata:")) {
                lines.add(new Line(lineNumber, 1D, true));
                while (iterator.hasNext() && countLeadingWhitespaces(currentLine = iterator.next()) >= indentation + 2) {
                    lineNumber++;
                    if (currentLine.trim().startsWith("labels:")) {
                        lines.add(new Line(lineNumber, 1D, true));
                        while (iterator.hasNext() && countLeadingWhitespaces(currentLine = iterator.next()) >= indentation + 4) {
                            lineNumber++;
                            String[] lineSplit = currentLine.split(":");
                            StringStringMap label = new StringStringMap(lineSplit[0].trim(), lineSplit[1].trim());
                            Set<StringStringMap> labels = podSpec.getLabels();
                            labels.add(label);
                            podSpec.setLabels(labels);
                            lines.add(new Line(lineNumber, 1D, true));
                        }
                        if (iterator.hasNext()) iterator.previous();
                    } else if (currentLine.trim().startsWith("#")) {
                        continue;
                    } else {
                        lines.add(new Line(lineNumber, 0D, true));
                    }
                }
                if (iterator.hasNext()) iterator.previous();
            } else if (currentLine.trim().startsWith("spec:")) {
                lines.add(new Line(lineNumber, 1D, true));
                while (iterator.hasNext() && countLeadingWhitespaces(currentLine = iterator.next()) >= indentation + 2) {
                    lineNumber++;
                    if (currentLine.trim().startsWith("containers:")) {
                        lines.add(new Line(lineNumber, 1D, true));
                        while (iterator.hasNext() && (currentLine = iterator.next()).trim().startsWith("-")) {
                            lineNumber++;
                            Container container = new Container();
                            currentLine = currentLine.replaceFirst("-", " ");
                            while (countLeadingWhitespaces(currentLine) >= indentation + 4) {
                                if (currentLine.trim().startsWith("name:")) {
                                    lines.add(new Line(lineNumber, 1D, true));
                                    container.setName(currentLine.split(":")[1].trim());
                                } else if (currentLine.trim().startsWith("image:")) {
                                    lines.add(new Line(lineNumber, 1D, true));
                                    container.setImage(currentLine.split(":")[1].trim());
                                } else if (currentLine.trim().startsWith("imagePullPolicy:")) {
                                    lines.add(new Line(lineNumber, 1D, true));
                                    container.setImage(currentLine.split(":")[1].trim());
                                } else if (currentLine.trim().startsWith("ports:")) {
                                    lines.add(new Line(lineNumber, 1D, true));
                                    Set<ContainerPort> containerPorts = new HashSet<>();
                                    while (iterator.hasNext() && (currentLine = iterator.next()).trim().startsWith("-")) {
                                        ContainerPort containerPort = new ContainerPort();
                                        currentLine = currentLine.replaceFirst("-", " ");
                                        while (countLeadingWhitespaces(currentLine) >= indentation + 6) {
                                            lineNumber++;
                                            String[] lineSplitPort = currentLine.split(":");
                                            if (currentLine.trim().startsWith("name:")) {
                                                lines.add(new Line(lineNumber, 1D, true));
                                                containerPort.setName(lineSplitPort[1].trim());
                                            } else if (currentLine.trim().startsWith("containerPort:")) {
                                                lines.add(new Line(lineNumber, 1D, true));
                                                containerPort.setPort(Integer.parseInt(lineSplitPort[1].trim()));
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
                                    lines.add(new Line(lineNumber, 1D, true));
                                    Set<EnvironmentVariable> environmentVariables = new HashSet<>();
                                    while (iterator.hasNext() && (currentLine = iterator.next()).trim().startsWith("-")) {
                                        EnvironmentVariable environmentVariable = new EnvironmentVariable();
                                        currentLine = currentLine.replaceFirst("-", " ");
                                        while (countLeadingWhitespaces(currentLine) >= indentation + 6) {
                                            lineNumber++;
                                            if (currentLine.trim().startsWith("name:")) {
                                                lines.add(new Line(lineNumber, 1D, true));
                                                environmentVariable.setKey(currentLine.split("name:")[1].trim());
                                            } else if (currentLine.trim().startsWith("value:")) {
                                                lines.add(new Line(lineNumber, 1D, true));
                                                environmentVariable.setValue(currentLine.split("value:")[1].trim());
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
                                    container.setWorkingDir(currentLine.split("workingDir:")[1].trim());
                                } else if (currentLine.trim().startsWith("args:")) {
                                    lines.add(new Line(lineNumber, 1D, true));
                                    while (iterator.hasNext() && (currentLine = iterator.next()).trim().startsWith("-")) {
                                        lineNumber++;
                                        lines.add(new Line(lineNumber, 1D, true));
                                        container.getArgs().add(currentLine.trim().replace("-", ""));
                                    }
                                } else if (currentLine.trim().startsWith("command:")) {
                                    lines.add(new Line(lineNumber, 1D, true));
                                    while (iterator.hasNext() && (currentLine = iterator.next()).trim().startsWith("-")) {
                                        lineNumber++;
                                        lines.add(new Line(lineNumber, 1D, true));
                                        container.getCommand().add(currentLine.trim().replace("-", ""));
                                    }
                                } else if (currentLine.trim().startsWith("volumeMounts:")) {
                                    lines.add(new Line(lineNumber, 1D, true));
                                    while (iterator.hasNext() && countLeadingWhitespaces((currentLine = iterator.next()).replace("-", " ")) >= indentation + 6) {
                                        lineNumber++;
                                        VolumeMount volumeMount = new VolumeMount();
                                        if (currentLine.trim().startsWith("mountPath:")) {
                                            lines.add(new Line(lineNumber, 1D, true));
                                            volumeMount.setMountPath(currentLine.trim().split(":")[1]);
                                        } else if (currentLine.trim().startsWith("name:")) {
                                            lines.add(new Line(lineNumber, 1D, true));
                                            volumeMount.setName(currentLine.trim().split(":")[1]);
                                        } else if (currentLine.trim().startsWith("readOnly:")) {
                                            lines.add(new Line(lineNumber, 1D, true));
                                            volumeMount.setReadOnly(Boolean.parseBoolean((currentLine.trim().split(":")[1])));
                                        } else if (currentLine.trim().startsWith("subPath:")) {
                                            lines.add(new Line(lineNumber, 1D, true));
                                            volumeMount.setSubPath(currentLine.trim().split(":")[1]);
                                        } else if (currentLine.trim().startsWith("subPathExpr:")) {
                                            lines.add(new Line(lineNumber, 1D, true));
                                            volumeMount.setSubPathExpr(currentLine.trim().split(":")[1]);
                                        } else if (currentLine.trim().startsWith("mountPropagation:")) {
                                            lines.add(new Line(lineNumber, 1D, true));
                                            volumeMount.setMountPropagation(currentLine.trim().split(":")[1]);
                                        } else {
                                            lines.add(new Line(lineNumber, 0D, true));
                                        }
                                        container.getVolumeMounts().add(volumeMount);
                                    }
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
                        podSpec.setRestartPolicy(currentLine.split(":")[1].trim());
                    } else if (currentLine.trim().startsWith("volumes")) {
                        lines.add(new Line(lineNumber, 1D, true));
                        while (iterator.hasNext() && countLeadingWhitespaces((currentLine = iterator.next()).replace("-", " ")) >= indentation + 4) {
                            lineNumber++;
                            Volume volume = new Volume();
                            if (currentLine.trim().startsWith("name:")) {
                                lines.add(new Line(lineNumber, 1D, true));
                                volume.setName(currentLine.trim().split(":")[1]);
                            } else if (currentLine.trim().startsWith("persistentVolumeClaim:")) {
                                lines.add(new Line(lineNumber, 1D, true));
                                while (iterator.hasNext() &&
                                        countLeadingWhitespaces((currentLine = iterator.next())) >= indentation + 6) {
                                    lineNumber++;
                                    if (currentLine.trim().startsWith("claimName:")) {
                                        volume.setPersistentVolumeClaimName(currentLine.trim().split(":")[1]);
                                        lines.add(new Line(lineNumber, 1D, true));
                                    } else if (currentLine.trim().startsWith("readOnly:")) {
                                        lines.add(new Line(lineNumber, 1D, true));
                                        volume.setPersistentVolumeClaimReadOnly(Boolean.parseBoolean(currentLine.trim().split(
                                                ":")[1]));
                                    }
                                    if (iterator.hasNext()) iterator.previous();
                                }
                            } else {
                                lines.add(new Line(lineNumber, 0D, true));
                            }
                            podSpec.getVolumes().add(volume);
                        }
                    }
                    else {
                        lines.add(new Line(lineNumber, 0D, true));
                    }
                }
                if (iterator.hasNext()) {
                    iterator.previous();
                }
            } else if (currentLine.startsWith("kind:")) {
                lines.add(new Line(lineNumber, 1D, true));
            }
            else {
                lines.add(new Line(lineNumber, 0D, true));
            }
            lineNumber++;
        }
        Set<KubernetesPodSpec> podSpecs = deployment.getPodSpecs();
        podSpecs.add(podSpec);
        deployment.setPodSpecs(podSpecs);
        return lines;
    }
}
