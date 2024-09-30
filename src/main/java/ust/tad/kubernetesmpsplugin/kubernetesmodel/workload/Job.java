package ust.tad.kubernetesmpsplugin.kubernetesmodel.workload;

import ust.tad.kubernetesmpsplugin.kubernetesmodel.workload.pods.KubernetesPodSpec;

import java.util.Objects;

public class Job {
    private String name;
    private int parallelism;
    private KubernetesPodSpec podSpec;

    public Job() {}

    public Job(String name, int parallelism, KubernetesPodSpec podSpec) {
        this.name = name;
        this.parallelism = parallelism;
        this.podSpec = podSpec;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParallelism() {
        return parallelism;
    }

    public void setParallelism(int parallelism) {
        this.parallelism = parallelism;
    }

    public KubernetesPodSpec getPodSpec() {
        return podSpec;
    }

    public void setPodSpec(KubernetesPodSpec podSpec) {
        this.podSpec = podSpec;
    }

    public Job name(String name) {
        setName(name);
        return this;
    }

    public Job parallelism(int parallelism) {
        setParallelism(parallelism);
        return this;
    }

    public Job podSpec(KubernetesPodSpec podSpec) {
        setPodSpec(podSpec);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Job job = (Job) o;
        return Objects.equals(name, job.name) &&
                Objects.equals(parallelism, job.parallelism) &&
                Objects.equals(podSpec, job.podSpec);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, parallelism, podSpec);
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + "'" +
                ", parallelism=" + parallelism +
                ", podSpec=" + podSpec +
                "}";
    }
}
