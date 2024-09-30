package ust.tad.kubernetesmpsplugin.kubernetesmodel.configStorageResources;

import java.util.Objects;

public class PersistentVolumeClaim {
    private String volumeName;
    private String requests;
    private String limit;

    public PersistentVolumeClaim() {}

    public PersistentVolumeClaim(String volumeName, String requests, String limit) {
        this.volumeName = volumeName;
        this.requests = requests;
        this.limit = limit;
    }

    public String getVolumeName() {
        return volumeName;
    }

    public void setVolumeName(String volumeName) {
        this.volumeName = volumeName;
    }

    public String getRequests() {
        return requests;
    }

    public void setRequests(String requests) {
        this.requests = requests;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public PersistentVolumeClaim volumeName(String volumeName) {
        setVolumeName(volumeName);
        return this;
    }

    public PersistentVolumeClaim requests(String requests) {
        setRequests(requests);
        return this;
    }

    public PersistentVolumeClaim limit(String limit) {
        setLimit(limit);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersistentVolumeClaim pvc = (PersistentVolumeClaim) o;
        return Objects.equals(this.volumeName, pvc.volumeName) &&
                Objects.equals(this.limit, pvc.limit) &&
                Objects.equals(this.requests, pvc.requests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(volumeName, limit, requests);
    }

    @Override
    public String toString() {
        return "{" +
                " volumeName='" + getVolumeName() + "'" +
                ", limit='" + getLimit() + "'" +
                ", requests='" + getRequests() + "'" +
                "}";
    }

}
