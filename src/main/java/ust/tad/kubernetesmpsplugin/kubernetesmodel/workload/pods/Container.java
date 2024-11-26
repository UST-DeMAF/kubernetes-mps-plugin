package ust.tad.kubernetesmpsplugin.kubernetesmodel.workload.pods;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Container {
    private String name;
    private String image;
    private String imagePullPolicy = "IfNotPresent";
    private List<String> command = new ArrayList<>();
    private List<String> args = new ArrayList<>();
    private String workingDir;
    private Set<ContainerPort> containerPorts = new HashSet<>();
    private Set<EnvironmentVariable> environmentVariables = new HashSet<>();
    private Set<VolumeMount> volumeMounts = new HashSet<>();

    public Container() {}

    public Container(
            String name,
            String image,
            String imagePullPolicy,
            List<String> command,
            List<String> args,
            String workingDir,
            Set<ContainerPort> containerPorts,
            Set<EnvironmentVariable> environmentVariables,
            Set<VolumeMount> volumeMounts
    ) {
        this.name = name;
        this.image = image;
        this.imagePullPolicy = imagePullPolicy;
        this.command = command;
        this.args = args;
        this.workingDir = workingDir;
        this.containerPorts = containerPorts;
        this.environmentVariables = environmentVariables;
        this.volumeMounts = volumeMounts;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImagePullPolicy() {
        return this.imagePullPolicy;
    }

    public void setImagePullPolicy(String imagePullPolicy) {
        this.imagePullPolicy = imagePullPolicy;
    }

    public List<String> getCommand() {
        return this.command;
    }

    public void setCommand(List<String> command) {
        this.command = command;
    }

    public List<String> getArgs() {
        return this.args;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }

    public String getWorkingDir() {
        return this.workingDir;
    }

    public void setWorkingDir(String workingDir) {
        this.workingDir = workingDir;
    }

    public Set<ContainerPort> getContainerPorts() {
        return this.containerPorts;
    }

    public void setContainerPorts(Set<ContainerPort> containerPorts) {
        this.containerPorts = containerPorts;
    }

    public Set<EnvironmentVariable> getEnvironmentVariables() {
        return this.environmentVariables;
    }

    public void setEnvironmentVariables(Set<EnvironmentVariable> environmentVariables) {
        this.environmentVariables = environmentVariables;
    }

    public Set<VolumeMount> getVolumeMounts() {
        return this.volumeMounts;
    }

    public void setVolumeMounts(Set<VolumeMount> volumeMounts) {
        this.volumeMounts = volumeMounts;
    }

    public Container name(String name) {
        setName(name);
        return this;
    }

    public Container image(String image) {
        setImage(image);
        return this;
    }

    public Container imagePullPolicy(String imagePullPolicy) {
        setImage(imagePullPolicy);
        return this;
    }

    public Container containerPorts(Set<ContainerPort> containerPorts) {
        setContainerPorts(containerPorts);
        return this;
    }

    public Container environmentVariables(Set<EnvironmentVariable> environmentVariables) {
        setEnvironmentVariables(environmentVariables);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Container)) {
            return false;
        }
        Container container = (Container) o;
        return Objects.equals(name, container.name) &&
                Objects.equals(image, container.image) &&
                Objects.equals(imagePullPolicy, container.imagePullPolicy) &&
                Objects.equals(command, container.command) &&
                Objects.equals(args, container.args) &&
                Objects.equals(workingDir, container.workingDir) &&
                Objects.equals(containerPorts, container.containerPorts) &&
                Objects.equals(environmentVariables, container.environmentVariables) &&
                Objects.equals(volumeMounts, container.volumeMounts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                name,
                image,
                imagePullPolicy,
                command,
                args,
                workingDir,
                containerPorts,
                environmentVariables,
                volumeMounts
        );
    }

    @Override
    public String toString() {
        return "{" +
                " name='" + getName() + "'" +
                ", image='" + getImage() + "'" +
                ", imagePullPolicy='" +  getImagePullPolicy() + "'" +
                ", command=" + getCommand() +
                ", args=" + getArgs() +
                ", workingDir='" + getWorkingDir() + "'" +
                ", containerPorts=" + getContainerPorts() +
                ", environmentVariables=" + getEnvironmentVariables() +
                ", volumeMounts=" + getVolumeMounts() +
                "}";
    }
}
