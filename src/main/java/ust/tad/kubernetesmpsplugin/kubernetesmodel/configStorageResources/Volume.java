package ust.tad.kubernetesmpsplugin.kubernetesmodel.configStorageResources;

import java.util.Objects;

public class Volume {
    private String name;
    private String persistentVolumeClaimName;
    private boolean persistentVolumeClaimReadOnly;
    private String subPath;

    public Volume() {}

    public Volume(String name, String persistentVolumeClaimName, boolean persistentVolumeClaimReadOnly, String subPath) {
        this.name = name;
        this.persistentVolumeClaimName = persistentVolumeClaimName;
        this.persistentVolumeClaimReadOnly = persistentVolumeClaimReadOnly;
        this.subPath = subPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPersistentVolumeClaimName() {
        return persistentVolumeClaimName;
    }

    public void setPersistentVolumeClaimName(String persistentVolumeClaimName) {
        this.persistentVolumeClaimName = persistentVolumeClaimName;
    }

    public boolean isPersistentVolumeClaimReadOnly() {
        return persistentVolumeClaimReadOnly;
    }

    public void setPersistentVolumeClaimReadOnly(boolean persistentVolumeClaimReadOnly) {
        this.persistentVolumeClaimReadOnly = persistentVolumeClaimReadOnly;
    }

    public String getSubPath() {
        return subPath;
    }

    public void setSubPath(String subPath) {
        this.subPath = subPath;
    }

    public Volume name(String name) {
        setName(name);
        return this;
    }

    public Volume persistentVolumeClaimName(String persistentVolumeClaimName) {
        setPersistentVolumeClaimName(persistentVolumeClaimName);
        return this;
    }

    public Volume persistentVolumeClaimReadOnly(boolean persistentVolumeClaimReadOnly) {
        setPersistentVolumeClaimReadOnly(persistentVolumeClaimReadOnly);
        return this;
    }

    public Volume subPath(String subPath) {
        setSubPath(subPath);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Volume volume = (Volume) o;
        return Objects.equals(name, volume.name) &&
                Objects.equals(persistentVolumeClaimName, volume.persistentVolumeClaimName) &&
                Objects.equals(persistentVolumeClaimReadOnly, volume.persistentVolumeClaimReadOnly) &&
                Objects.equals(subPath, volume.subPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, persistentVolumeClaimName, persistentVolumeClaimReadOnly, subPath);
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + "'" +
                ", persistentVolumeClaimName='" + persistentVolumeClaimName + "'" +
                ", persistentVolumeClaimReadOnly=" + persistentVolumeClaimReadOnly +
                ", subPath='" + subPath + "'" +
                "}";
    }
}
