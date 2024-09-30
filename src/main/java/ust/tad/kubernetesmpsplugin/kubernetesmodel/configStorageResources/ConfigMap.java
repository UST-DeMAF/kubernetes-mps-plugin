package ust.tad.kubernetesmpsplugin.kubernetesmodel.configStorageResources;

import ust.tad.kubernetesmpsplugin.kubernetesmodel.common.ObjectMeta;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.common.types.StringByteListMap;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.common.types.StringStringMap;

import java.util.Objects;

public class ConfigMap {
    private boolean immutable;
    private String name;
    private ObjectMeta metadata;
    private StringStringMap data;

    public ConfigMap() {}

    public ConfigMap(String name, boolean immutable, ObjectMeta metadata, StringStringMap data) {
        this.name = name;
        this.immutable = immutable;
        this.metadata = metadata;
        this.data = data;
    }

    public boolean isImmutable() {
        return immutable;
    }

    public void setImmutable(boolean immutable) {
        this.immutable = immutable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObjectMeta getMetadata() {
        return metadata;
    }

    public void setMetadata(ObjectMeta metadata) {
        this.metadata = metadata;
    }

    public StringStringMap getData() {
        return data;
    }

    public void setData(StringStringMap data) {
        this.data = data;
    }

    public ConfigMap name(String name) {
        setName(name);
        return this;
    }

    public ConfigMap immutable(boolean immutable) {
        setImmutable(immutable);
        return this;
    }

    public ConfigMap metadata(ObjectMeta metadata) {
        setMetadata(metadata);
        return this;
    }

    public ConfigMap data(StringStringMap data) {
        setData(data);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigMap configMap = (ConfigMap) o;
        return Objects.equals(this.name, configMap.name) &&
                Objects.equals(this.immutable, configMap.immutable) &&
                Objects.equals(this.metadata, configMap.metadata) &&
                Objects.equals(this.data, configMap.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, immutable, metadata, data);
    }

    @Override
    public String toString() {
        return "{" +
                "immutable=" + immutable +
                ", name='" + name + "'" +
                ", metadata=" + metadata +
                ", data=" + data +
                "}";
    }
}
