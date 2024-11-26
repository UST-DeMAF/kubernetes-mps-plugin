package ust.tad.kubernetesmpsplugin.kubernetesmodel.workload.pods;

import java.util.Objects;

public class VolumeMount {
    private String mountPath;
    private String name;
    private String mountPropagation = "MountPropagationNone";
    private boolean readOnly= false;
    private String subPath = "";
    private String subPathExpr = "";

    public VolumeMount() {}

    public String getMountPath() {
        return this.mountPath;
    }

    public void setMountPath(String mountPath) {
        this.mountPath = mountPath;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMountPropagation() {
        return this.mountPropagation;
    }

    public void setMountPropagation(String mountPropagation) {
        this.mountPropagation = mountPropagation;
    }

    public boolean getReadyOnly() {
        return this.readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public String getSubPath() {
        return this.subPath;
    }

    public void setSubPath(String subPath) {
        this.subPath = subPath;
    }

    public String getSubPathExpr() {
        return this.subPathExpr;
    }

    public void setSubPathExpr(String subPathExpr) {
        this.subPathExpr = subPathExpr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VolumeMount volumeMount = (VolumeMount) o;
        return Objects.equals(mountPath, volumeMount.mountPath) &&
                Objects.equals(name, volumeMount.name) &&
                Objects.equals(mountPropagation, volumeMount.mountPropagation) &&
                Objects.equals(readOnly, volumeMount.readOnly) &&
                Objects.equals(subPath, volumeMount.subPath) &&
                Objects.equals(subPathExpr, volumeMount.subPathExpr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mountPath, name, mountPropagation, readOnly, subPath, subPathExpr);
    }

    @Override
    public String toString() {
        return "{" +
                "mountPath='" + mountPath + "'" +
                ", name='" + name + "'" +
                ", mountPropagation='" + mountPropagation + "'" +
                ", readOnly=" + readOnly +
                ", subPath='" + subPath + "'" +
                ", subPathExpr='" + subPathExpr + "'" +
                "}";
    }

}
