package ust.tad.kubernetesmpsplugin.kubernetesmodel.ingress;

import java.util.Objects;

public class IngressBackend {
    private String serviceName;
    private String servicePortName;
    private int servicePortPort;

    public IngressBackend() {}

    public IngressBackend(String serviceName, String servicePortName, int servicePortPort) {
        this.serviceName = serviceName;
        this.servicePortName = servicePortName;
        this.servicePortPort = servicePortPort;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServicePortName() {
        return this.servicePortName;
    }

    public void setServicePortName(String servicePortName) {
        this.servicePortName = servicePortName;
    }

    public int getServicePortPort() {
        return this.servicePortPort;
    }

    public void setServicePortPort(int servicePortPort) {
        this.servicePortPort = servicePortPort;
    }

    public IngressBackend serviceName(String serviceName) {
        setServiceName(serviceName);
        return this;
    }

    public IngressBackend servicePortName(String servicePortName) {
        setServicePortName(servicePortName);
        return this;
    }

    public IngressBackend servicePortPort(int servicePortPort) {
        setServicePortPort(servicePortPort);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngressBackend ingressBackend = (IngressBackend) o;
        return Objects.equals(serviceName, ingressBackend.serviceName) &&
                Objects.equals(servicePortName, ingressBackend.servicePortName) &&
                Objects.equals(servicePortPort, ingressBackend.servicePortPort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceName, servicePortName, servicePortPort);
    }

    @Override
    public String toString() {
        return "{" +
                " serviceName='" + getServiceName() + "'" +
                ", servicePortName='" + getServicePortName() + "'" +
                ", servicePortPort='" + getServicePortPort() + "'" +
                "}";
    }
}
