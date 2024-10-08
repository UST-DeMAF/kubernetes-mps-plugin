package ust.tad.kubernetesmpsplugin.kubernetesmodel.service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class KubernetesService {

  private String name;

  private Set<ServicePort> servicePorts = new HashSet<>();

  private Set<Selector> selectors = new HashSet<>();

  public KubernetesService() {}

  public KubernetesService(String name, Set<ServicePort> servicePorts, Set<Selector> selectors) {
    this.name = name;
    this.servicePorts = servicePorts;
    this.selectors = selectors;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<ServicePort> getServicePorts() {
    return this.servicePorts;
  }

  public void setServicePorts(Set<ServicePort> servicePorts) {
    this.servicePorts = servicePorts;
  }

  public Set<Selector> getSelectors() {
    return this.selectors;
  }

  public void setSelectors(Set<Selector> selectors) {
    this.selectors = selectors;
  }

  public KubernetesService name(String name) {
    setName(name);
    return this;
  }

  public KubernetesService servicePorts(Set<ServicePort> servicePorts) {
    setServicePorts(servicePorts);
    return this;
  }

  public KubernetesService selectors(Set<Selector> selectors) {
    setSelectors(selectors);
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof KubernetesService)) {
      return false;
    }
    KubernetesService service = (KubernetesService) o;
    return Objects.equals(name, service.name)
        && Objects.equals(servicePorts, service.servicePorts)
        && Objects.equals(selectors, service.selectors);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, servicePorts, selectors);
  }

  @Override
  public String toString() {
    return "{"
        + " name='"
        + getName()
        + "'"
        + ", servicePorts='"
        + getServicePorts()
        + "'"
        + ", selectors='"
        + getSelectors()
        + "'"
        + "}";
  }
}
