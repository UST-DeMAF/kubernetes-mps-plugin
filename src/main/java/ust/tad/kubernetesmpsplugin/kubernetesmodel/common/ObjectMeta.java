package ust.tad.kubernetesmpsplugin.kubernetesmodel.common;

import ust.tad.kubernetesmpsplugin.kubernetesmodel.common.types.StringStringMap;

import java.util.Objects;

public class ObjectMeta {
    private String name;
    private String namespace;
    private StringStringMap labels;
    private StringStringMap annotations;

    public ObjectMeta() {}

    public ObjectMeta(String name, String namespace, StringStringMap labels, StringStringMap annotations) {
        this.name = name;
        this.namespace = namespace;
        this.labels = labels;
        this.annotations = annotations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public StringStringMap getLabels() {
        return labels;
    }

    public void setLabels(StringStringMap labels) {
        this.labels = labels;
    }

    public StringStringMap getAnnotations() {
        return annotations;
    }

    public void setAnnotations(StringStringMap annotations) {
        this.annotations = annotations;
    }

    public ObjectMeta name(String name) {
        setName(name);
        return this;
    }

    public ObjectMeta namespace(String namespace) {
        setNamespace(namespace);
        return this;
    }

    public ObjectMeta labels(StringStringMap labels) {
        setLabels(labels);
        return this;
    }

    public ObjectMeta annotations(StringStringMap annotations) {
        setAnnotations(annotations);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectMeta objectMeta = (ObjectMeta) o;
        return Objects.equals(this.name, objectMeta.name) &&
                Objects.equals(this.namespace, objectMeta.namespace) &&
                Objects.equals(this.labels, objectMeta.labels) &&
                Objects.equals(this.annotations, objectMeta.annotations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, namespace, labels, annotations);
    }

    @Override
    public String toString() {
        return "{" +
                " name='" + getName() + "'" +
                ", namespace='" + getNamespace() + "'" +
                ", labels='" + getLabels() + "'" +
                ", annotations='" + getAnnotations() + "'" +
                "}";
    }
}
