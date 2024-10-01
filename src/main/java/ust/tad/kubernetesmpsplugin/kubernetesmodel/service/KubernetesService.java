package ust.tad.kubernetesmpsplugin.kubernetesmodel.service;

import ust.tad.kubernetesmpsplugin.kubernetesmodel.common.types.StringStringMap;

import java.util.HashSet;
import java.util.Objects;
import java.util.List;
import java.util.Set;

public class KubernetesService {
    private String name;
    private String type;
    private String ipFamilyPolicy;
    private String clusterIP;
    private String sessionAffinity;
    private String loadBalancerIP;
    private String externalName;
    private String externalTrafficPolicy;
    private String internalTrafficPolicy;
    private int healthCheckNodePort;
    private Set<ServicePort> servicePorts;
    private Set<StringStringMap> selectors;
    private Set<String> ipFamilies;
    private Set<String> clusterIPs;
    private Set<String> externalIPs;
    private Set<String> loadBalancerSourceRanges;

    public KubernetesService() {}

    public KubernetesService(
            String name,
            String type,
            String ipFamilyPolicy,
            String clusterIP,
            String sessionAffinity,
            String loadBalancerIP,
            String externalName,
            String externalTrafficPolicy,
            String internalTrafficPolicy,
            int healthCheckNodePort,
            Set<ServicePort> servicePorts,
            Set<StringStringMap> selectors,
            Set<String> ipFamilies,
            Set<String> clusterIPs,
            Set<String> externalIPs,
            Set<String> loadBalancerSourceRanges
    ) {
        this.name = name;
        this.type = type;
        this.ipFamilyPolicy = ipFamilyPolicy;
        this.clusterIP = clusterIP;
        this.sessionAffinity = sessionAffinity;
        this.loadBalancerIP = loadBalancerIP;
        this.externalName = externalName;
        this.externalTrafficPolicy = externalTrafficPolicy;
        this.internalTrafficPolicy = internalTrafficPolicy;
        this.healthCheckNodePort = healthCheckNodePort;
        this.servicePorts = servicePorts;
        this.selectors = selectors;
        this.ipFamilies = ipFamilies;
        this.clusterIPs = clusterIPs;
        this.externalIPs = externalIPs;
        this.loadBalancerSourceRanges = loadBalancerSourceRanges;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIpFamilyPolicy() {
        return ipFamilyPolicy;
    }

    public void setIpFamilyPolicy(String ipFamilyPolicy) {
        this.ipFamilyPolicy = ipFamilyPolicy;
    }

    public String getClusterIP() {
        return clusterIP;
    }

    public void setClusterIP(String clusterIP) {
        this.clusterIP = clusterIP;
    }

    public String getSessionAffinity() {
        return sessionAffinity;
    }

    public void setSessionAffinity(String sessionAffinity) {
        this.sessionAffinity = sessionAffinity;
    }

    public String getLoadBalancerIP() {
        return loadBalancerIP;
    }

    public void setLoadBalancerIP(String loadBalancerIP) {
        this.loadBalancerIP = loadBalancerIP;
    }

    public String getExternalName() {
        return externalName;
    }

    public void setExternalName(String externalName) {
        this.externalName = externalName;
    }

    public String getExternalTrafficPolicy() {
        return externalTrafficPolicy;
    }

    public void setExternalTrafficPolicy(String externalTrafficPolicy) {
        this.externalTrafficPolicy = externalTrafficPolicy;
    }

    public String getInternalTrafficPolicy() {
        return internalTrafficPolicy;
    }

    public void setInternalTrafficPolicy(String internalTrafficPolicy) {
        this.internalTrafficPolicy = internalTrafficPolicy;
    }

    public int getHealthCheckNodePort() {
        return healthCheckNodePort;
    }

    public void setHealthCheckNodePort(int healthCheckNodePort) {
        this.healthCheckNodePort = healthCheckNodePort;
    }

    public Set<ServicePort> getServicePorts() {
        return servicePorts;
    }

    public void setServicePorts(Set<ServicePort> servicePorts) {
        this.servicePorts = servicePorts;
    }

    public Set<StringStringMap> getSelectors() {
        return selectors;
    }

    public void setSelectors(Set<StringStringMap> selectors) {
        this.selectors = selectors;
    }

    public Set<String> getIpFamilies() {
        return ipFamilies;
    }

    public void setIpFamilies(Set<String> ipFamilies) {
        this.ipFamilies = ipFamilies;
    }

    public Set<String> getClusterIPs() {
        return clusterIPs;
    }

    public void setClusterIPs(Set<String> clusterIPs) {
        this.clusterIPs = clusterIPs;
    }

    public Set<String> getExternalIPs() {
        return externalIPs;
    }

    public void setExternalIPs(Set<String> externalIPs) {
        this.externalIPs = externalIPs;
    }

    public Set<String> getLoadBalancerSourceRanges() {
        return loadBalancerSourceRanges;
    }

    public void setLoadBalancerSourceRanges(Set<String> loadBalancerSourceRanges) {
        this.loadBalancerSourceRanges = loadBalancerSourceRanges;
    }


    public KubernetesService name(String name) {
        setName(name);
        return this;
    }

    public KubernetesService type(String type) {
        setType(type);
        return this;
    }

    public KubernetesService ipFamilyPolicy(String ipFamilyPolicy) {
        setIpFamilyPolicy(ipFamilyPolicy);
        return this;
    }

    public KubernetesService clusterIP(String clusterIP) {
        setClusterIP(clusterIP);
        return this;
    }

    public KubernetesService sessionAffinity(String sessionAffinity) {
        setSessionAffinity(sessionAffinity);
        return this;
    }

    public KubernetesService loadBalancerIP(String loadBalancerIP) {
        setLoadBalancerIP(loadBalancerIP);
        return this;
    }

    public KubernetesService externalName(String externalName) {
        setExternalName(externalName);
        return this;
    }

    public KubernetesService externalTrafficPolicy(String externalTrafficPolicy) {
        setExternalTrafficPolicy(externalTrafficPolicy);
        return this;
    }

    public KubernetesService internalTrafficPolicy(String internalTrafficPolicy) {
        setInternalTrafficPolicy(internalTrafficPolicy);
        return this;
    }

    public KubernetesService healthCheckNodePort(int healthCheckNodePort) {
        setHealthCheckNodePort(healthCheckNodePort);
        return this;
    }

    public KubernetesService servicePorts(Set<ServicePort> servicePorts) {
        setServicePorts(servicePorts);
        return this;
    }

    public KubernetesService selectors(Set<StringStringMap> selectors) {
        setSelectors(selectors);
        return this;
    }

    public KubernetesService ipFamilies(Set<String> ipFamilies) {
        setIpFamilies(ipFamilies);
        return this;
    }

    public KubernetesService clusterIPs(Set<String> clusterIPs) {
        setClusterIPs(clusterIPs);
        return this;
    }

    public KubernetesService externalIPs(Set<String> externalIPs) {
        setExternalIPs(externalIPs);
        return this;
    }

    public KubernetesService loadBalancerSourceRanges(Set<String> loadBalancerSourceRanges) {
        setLoadBalancerSourceRanges(loadBalancerSourceRanges);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KubernetesService service = (KubernetesService) o;
        return Objects.equals(name, service.name) &&
                Objects.equals(type, service.type) &&
                Objects.equals(ipFamilyPolicy, service.ipFamilyPolicy) &&
                Objects.equals(clusterIP, service.clusterIP) &&
                Objects.equals(sessionAffinity, service.sessionAffinity) &&
                Objects.equals(loadBalancerIP, service.loadBalancerIP) &&
                Objects.equals(externalName, service.externalName) &&
                Objects.equals(externalTrafficPolicy, service.externalTrafficPolicy) &&
                Objects.equals(internalTrafficPolicy, service.internalTrafficPolicy) &&
                Objects.equals(healthCheckNodePort, service.healthCheckNodePort) &&
                Objects.equals(servicePorts, service.servicePorts) &&
                Objects.equals(selectors, service.selectors) &&
                Objects.equals(ipFamilies, service.ipFamilies) &&
                Objects.equals(clusterIPs, service.clusterIPs) &&
                Objects.equals(externalIPs, service.externalIPs) &&
                Objects.equals(loadBalancerSourceRanges, service.loadBalancerSourceRanges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, ipFamilyPolicy, clusterIP, sessionAffinity, loadBalancerIP, externalName, externalTrafficPolicy, internalTrafficPolicy, healthCheckNodePort, servicePorts, selectors, ipFamilies, clusterIPs, externalIPs, loadBalancerSourceRanges);
    }

    @Override
    public String toString() {
        return "{" +
                " name='" + name + "'" +
                ", type='" + type + "'" +
                ", ipFamilyPolicy='" + ipFamilyPolicy + "'" +
                ", clusterIP='" + clusterIP + "'" +
                ", sessionAffinity='" + sessionAffinity + "'" +
                ", loadBalancerIP='" + loadBalancerIP + "'" +
                ", externalName='" + externalName + "'" +
                ", externalTrafficPolicy='" + externalTrafficPolicy + "'" +
                ", internalTrafficPolicy='" + internalTrafficPolicy + "'" +
                ", healthCheckNodePort='" + healthCheckNodePort + "'" +
                ", servicePorts=" + servicePorts +
                ", selectors=" + selectors +
                ", ipFamilies=" + ipFamilies +
                ", clusterIPs=" + clusterIPs +
                ", externalIPs=" + externalIPs +
                ", loadBalancerSourceRanges=" + loadBalancerSourceRanges +
                "}";
    }

    
}
