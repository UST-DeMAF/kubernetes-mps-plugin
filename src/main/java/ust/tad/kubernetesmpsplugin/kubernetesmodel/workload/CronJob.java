package ust.tad.kubernetesmpsplugin.kubernetesmodel.workload;

import ust.tad.kubernetesmpsplugin.kubernetesmodel.common.types.StringStringMap;

import java.util.Objects;

public class CronJob {
    private String name;
    private String schedule;
    private String timezone;
    private String concurrentPolicy;
    private int startingDeadlineSeconds;
    private Job job;
    private StringStringMap labels;

    private CronJob() {}

    private CronJob(String name, String schedule, String timezone, String concurrentPolicy, int startingDeadlineSeconds, Job job, StringStringMap labels) {
        this.name = name;
        this.schedule = schedule;
        this.timezone = timezone;
        this.concurrentPolicy = concurrentPolicy;
        this.startingDeadlineSeconds = startingDeadlineSeconds;
        this.job = job;
        this.labels = labels;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getSchedule() {
        return schedule;
    }

    private void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getTimezone() {
        return timezone;
    }

    private void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getConcurrentPolicy() {
        return concurrentPolicy;
    }

    private void setConcurrentPolicy(String concurrentPolicy) {
        this.concurrentPolicy = concurrentPolicy;
    }

    public int getStartingDeadlineSeconds() {
        return startingDeadlineSeconds;
    }

    private void setStartingDeadlineSeconds(int startingDeadlineSeconds) {
        this.startingDeadlineSeconds = startingDeadlineSeconds;
    }

    public Job getJob() {
        return job;
    }

    private void setJob(Job job) {
        this.job = job;
    }

    public StringStringMap getLabels() {
        return labels;
    }

    private void setLabels(StringStringMap labels) {
        this.labels = labels;
    }

    public CronJob name(String name) {
        setName(name);
        return this;
    }

    public CronJob schedule(String schedule) {
        setSchedule(schedule);
        return this;
    }

    public CronJob timezone(String timezone) {
        setTimezone(timezone);
        return this;
    }

    public CronJob concurrentPolicy(String concurrentPolicy) {
        setConcurrentPolicy(concurrentPolicy);
        return this;
    }

    public CronJob startingDeadlineSeconds(int startingDeadlineSeconds) {
        setStartingDeadlineSeconds(startingDeadlineSeconds);
        return this;
    }

    public CronJob job(Job job) {
        setJob(job);
        return this;
    }

    public CronJob labels(StringStringMap labels) {
        setLabels(labels);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CronJob cronJob = (CronJob) o;
        return Objects.equals(name, cronJob.name) &&
                Objects.equals(schedule, cronJob.schedule) &&
                Objects.equals(timezone, cronJob.timezone) &&
                Objects.equals(concurrentPolicy, cronJob.concurrentPolicy) &&
                Objects.equals(startingDeadlineSeconds, cronJob.startingDeadlineSeconds) &&
                Objects.equals(job, cronJob.job) &&
                Objects.equals(labels, cronJob.labels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, schedule, timezone, concurrentPolicy, startingDeadlineSeconds, job, labels);
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + "'" +
                ", schedule='" + schedule + "'" +
                ", timezone='" + timezone + "'" +
                ", concurrentPolicy='" + concurrentPolicy + "'" +
                ", startingDeadlineSeconds=" + startingDeadlineSeconds +
                ", job=" + job +
                ", labels=" + labels +
                "}";
    }
}
