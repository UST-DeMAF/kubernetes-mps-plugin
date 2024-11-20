package ust.tad.kubernetesmpsplugin.kubernetesmodel.workload.pods;

import ust.tad.kubernetesmpsplugin.kubernetesmodel.common.types.StringStringMap;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.configStorageResources.Volume;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class KubernetesPodSpec {
    private String hostname;
    private String restartPolicy;
    private Set<Container> containers = new HashSet<>();
    private Set<Container> initContainers = new HashSet<>();
    private Set<StringStringMap> labels = new HashSet<>();
    private Set<Volume> volumes = new HashSet<>();

    public KubernetesPodSpec() {}

    public KubernetesPodSpec(String hostname, String restartPolicy, Set<Container> containers, Set<Container> initContainers, Set<Volume> volumes) {
        this.hostname = hostname;
        this.restartPolicy = restartPolicy;
        this.containers = containers;
        this.initContainers = initContainers;
        this.volumes = volumes;
    }

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getRestartPolicy() {
        return this.restartPolicy;
    }

    public void setRestartPolicy(String restartPolicy) {
        this.restartPolicy = restartPolicy;
    }

    public Set<Container> getContainers() {
        return this.containers;
    }

    public void setContainers(Set<Container> containers) {
        this.containers = containers;
    }

    public Set<Container> getInitContainers() {
        return this.initContainers;
    }

    public void setInitContainers(Set<Container> initContainers) {
        this.initContainers = initContainers;
    }

    public Set<StringStringMap> getLabels() { return this.labels; }

    public void setLabels(Set<StringStringMap> labels) { this.labels = labels; }

    public Set<Volume> getVolumes() { return this.volumes; }

    public void setVolumes(Set<Volume> volumes) { this.volumes = volumes; }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof KubernetesPodSpec)) {
            return false;
        }
        KubernetesPodSpec podSpec = (KubernetesPodSpec) o;
        return Objects.equals(hostname, podSpec.hostname) && Objects.equals(restartPolicy, podSpec.restartPolicy) && Objects.equals(containers, podSpec.containers) && Objects.equals(initContainers, podSpec.initContainers) && Objects.equals(volumes, podSpec.volumes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hostname, restartPolicy, containers, initContainers, volumes);
    }

    @Override
    public String toString() {
        return "{" +
                " hostname='" + getHostname() + "'" +
                ", restartPolicy='" + getRestartPolicy() + "'" +
                ", containers=" + getContainers() +
                ", initContainers=" + getInitContainers() +
                ", volumes=" + getVolumes() +
                "}";
    }
}
