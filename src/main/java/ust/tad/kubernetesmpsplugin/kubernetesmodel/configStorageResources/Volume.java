package ust.tad.kubernetesmpsplugin.kubernetesmodel.configStorageResources;

import java.util.Objects;

public class Volume {
    private String name;
    private String persistentVolumeClaimName = "";
    private boolean persistentVolumeClaimReadOnly = false;

    public Volume() {}

    public Volume(String name, String persistentVolumeClaimName, boolean persistentVolumeClaimReadOnly) {
        this.name = name;
        this.persistentVolumeClaimName = persistentVolumeClaimName;
        this.persistentVolumeClaimReadOnly = persistentVolumeClaimReadOnly;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Volume volume = (Volume) o;
        return Objects.equals(name, volume.name) &&
                Objects.equals(persistentVolumeClaimName, volume.persistentVolumeClaimName) &&
                Objects.equals(persistentVolumeClaimReadOnly, volume.persistentVolumeClaimReadOnly);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, persistentVolumeClaimName, persistentVolumeClaimReadOnly);
    }

    @Override
    public String toString() {
        return "Volume{" +
                "name='" + name + '\'' +
                ", persistentVolumeClaimName='" + persistentVolumeClaimName + '\'' +
                ", persistentVolumeClaimReadOnly=" + persistentVolumeClaimReadOnly +
                '}';
    }
}
