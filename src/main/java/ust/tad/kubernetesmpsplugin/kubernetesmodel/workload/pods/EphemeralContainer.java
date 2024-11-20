package ust.tad.kubernetesmpsplugin.kubernetesmodel.workload.pods;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class EphemeralContainer extends Container {
    private String targetContainer;

    public EphemeralContainer() {}

    public EphemeralContainer(
            String name,
            String image,
            String imagePullPolicy,
            List<String> command,
            List<String> args,
            String workingDir,
            Set<ContainerPort> containerPorts,
            Set<EnvironmentVariable> environmentVariables,
            Set<VolumeMount> volumeMounts,
            String targetContainer
    ) {
        super(
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
        this.targetContainer = targetContainer;
    }

    public String getTargetContainer() {
        return targetContainer;
    }

    public void setTargetContainer(String targetContainer) {
        this.targetContainer = targetContainer;
    }

    public EphemeralContainer targetContainer(String targetContainer) {
        setTargetContainer(targetContainer);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EphemeralContainer container = (EphemeralContainer) o;
        return super.equals(container) &&
                Objects.equals(targetContainer, container.targetContainer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), targetContainer);
    }

    @Override
    public String toString() {
        return "{" +
                " name='" + getName() + "'" +
                ", image='" + getImage() + "'" +
                ", imagePullPolicy='" + getImagePullPolicy() + "'" +
                ", command=" + getCommand() +
                ", args=" + getArgs() +
                ", workingDir='" + getWorkingDir() + "'" +
                ", containerPorts=" + getContainerPorts() +
                ", environmentVariables=" + getEnvironmentVariables() +
                ", volumeMounts=" + getVolumeMounts() +
                ", targetContainer='" + getTargetContainer() + "'" +
                "}";
    }
}
