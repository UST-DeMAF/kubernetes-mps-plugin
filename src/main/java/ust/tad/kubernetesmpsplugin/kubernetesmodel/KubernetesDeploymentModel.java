package ust.tad.kubernetesmpsplugin.kubernetesmodel;

import ust.tad.kubernetesmpsplugin.kubernetesmodel.configStorageResources.ConfigMap;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.configStorageResources.PersistentVolumeClaim;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.ingress.KubernetesIngress;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.workload.deployment.KubernetesDeployment;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.service.KubernetesService;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.workload.pods.KubernetesPodSpec;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class KubernetesDeploymentModel {
  private Set<KubernetesDeployment> deployments = new HashSet<>();
  private Set<KubernetesService> services = new HashSet<>();
  private Set<KubernetesPodSpec> pods = new HashSet<>();
  private Set<KubernetesIngress> ingresses = new HashSet<>();
  private Set<PersistentVolumeClaim> persistentVolumeClaims = new HashSet<>();
  private Set<ConfigMap> configMaps = new HashSet<>();

  public KubernetesDeploymentModel() {}

  public KubernetesDeploymentModel(Set<KubernetesDeployment> deployments,
                                   Set<KubernetesService> services, Set<KubernetesPodSpec> pods,
                                   Set<KubernetesIngress> ingresses,
                                   Set<PersistentVolumeClaim> persistentVolumeClaims,
                                   Set<ConfigMap> configMaps) {
    this.deployments = deployments;
    this.services = services;
    this.pods = pods;
    this.ingresses = ingresses;
    this.persistentVolumeClaims = persistentVolumeClaims;
    this.configMaps = configMaps;
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

  public Set<KubernetesPodSpec> getPods() {
    return pods;
  }

  public void setPods(Set<KubernetesPodSpec> pods) {
    this.pods = pods;
  }

  public Set<KubernetesIngress> getIngresses() {
    return ingresses;
  }

  public void setIngresses(Set<KubernetesIngress> ingresses) {
    this.ingresses = ingresses;
  }

  public Set<PersistentVolumeClaim> getPersistentVolumeClaims() {
    return persistentVolumeClaims;
  }

  public void setPersistentVolumeClaims(Set<PersistentVolumeClaim> persistentVolumeClaims) {
    this.persistentVolumeClaims = persistentVolumeClaims;
  }

  public Set<ConfigMap> getConfigMaps() {
    return configMaps;
  }

  public void setConfigMaps(Set<ConfigMap> configMaps) {
    this.configMaps = configMaps;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    KubernetesDeploymentModel that = (KubernetesDeploymentModel) o;
    return Objects.equals(deployments, that.deployments) && Objects.equals(services,
            that.services) && Objects.equals(pods, that.pods) && Objects.equals(ingresses,
            that.ingresses) && Objects.equals(persistentVolumeClaims,
            that.persistentVolumeClaims) && Objects.equals(configMaps, that.configMaps);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deployments, services, pods, ingresses, persistentVolumeClaims, configMaps);
  }

  @Override
  public String toString() {
    return "KubernetesDeploymentModel{" +
            "deployments=" + deployments +
            ", services=" + services +
            ", pods=" + pods +
            ", ingresses=" + ingresses +
            ", persistentVolumeClaims=" + persistentVolumeClaims +
            ", configMaps=" + configMaps +
            '}';
  }
}
