package ust.tad.kubernetesmpsplugin.kubernetesmodel.ingress;

import java.util.Objects;

public class HTTPIngressRuleValue {
    private String path;
    private String pathType;
    private IngressBackend ingressBackend;

    public HTTPIngressRuleValue() {}

    public HTTPIngressRuleValue(String path, String pathType, IngressBackend ingressBackend) {
        this.path = path;
        this.pathType = pathType;
        this.ingressBackend = ingressBackend;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPathType() {
        return this.pathType;
    }

    public void setPathType(String pathType) {
        this.pathType = pathType;
    }

    public IngressBackend getIngressBackend() {
        return this.ingressBackend;
    }

    public void setIngressBackend(IngressBackend ingressBackend) {
        this.ingressBackend = ingressBackend;
    }

    public HTTPIngressRuleValue path(String path) {
        setPath(path);
        return this;
    }

    public HTTPIngressRuleValue pathType(String pathType) {
        setPathType(pathType);
        return this;
    }

    public HTTPIngressRuleValue ingressBackend(IngressBackend ingressBackend) {
        setIngressBackend(ingressBackend);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HTTPIngressRuleValue value = (HTTPIngressRuleValue) o;
        return Objects.equals(path, value.path) &&
                Objects.equals(pathType, value.pathType) &&
                Objects.equals(ingressBackend, value.ingressBackend);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, pathType, ingressBackend);
    }

    @Override
    public String toString() {
        return "{" +
                " path='" + getPath() + "'" +
                ", pathType='" + getPathType() + "'" +
                ", ingressBackend='" + getIngressBackend() + "'" +
                "}";
    }




}
