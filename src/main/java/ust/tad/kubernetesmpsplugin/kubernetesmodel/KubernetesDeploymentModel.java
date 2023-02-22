package ust.tad.kubernetesmpsplugin.kubernetesmodel;

import ust.tad.kubernetesmpsplugin.kubernetesmodel.deployment.KubernetesDeployment;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.service.KubernetesService;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class KubernetesDeploymentModel {

    private Set<KubernetesDeployment> deployments = new HashSet<>();

    private Set<KubernetesService> services = new HashSet<>();

    public KubernetesDeploymentModel() {
    }

    public KubernetesDeploymentModel(Set<KubernetesDeployment> deployments, Set<KubernetesService> services) {
        this.deployments = deployments;
        this.services = services;
    }

    public Set<KubernetesDeployment> getDeployments() {
        return deployments;
    }

    public void setDeployments(Set<KubernetesDeployment> deployments) {
        this.deployments = deployments;
    }

    public Set<KubernetesService> getServices() {
        return services;
    }

    public void setServices(Set<KubernetesService> services) {
        this.services = services;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KubernetesDeploymentModel that = (KubernetesDeploymentModel) o;
        return Objects.equals(deployments, that.deployments) && Objects.equals(services, that.services);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deployments, services);
    }

    @Override
    public String toString() {
        return "KubernetesDeploymentModel{" +
                "deployments=" + deployments +
                ", services=" + services +
                '}';
    }

}
