package ust.tad.kubernetesmpsplugin.kubernetesmodel.ingress;

import java.util.List;
import java.util.Objects;

public class IngressRule {
    private String host;
    private List<HTTPIngressRuleValue> ingressRuleValues;

    public IngressRule() {}

    public IngressRule(String host, List<HTTPIngressRuleValue> ingressRuleValues) {
        this.host = host;
        this.ingressRuleValues = ingressRuleValues;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public List<HTTPIngressRuleValue> getIngressRuleValues() {
        return this.ingressRuleValues;
    }

    public void setIngressRuleValues(List<HTTPIngressRuleValue> ingressRuleValues) {
        this.ingressRuleValues = ingressRuleValues;
    }

    public IngressRule host(String host) {
        this.host = host;
        return this;
    }

    public IngressRule ingressRuleValues(List<HTTPIngressRuleValue> ingressRuleValues) {
        this.ingressRuleValues = ingressRuleValues;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngressRule ingressRule = (IngressRule) o;
        return Objects.equals(this.host, ingressRule.host) &&
                Objects.equals(this.ingressRuleValues, ingressRule.ingressRuleValues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, ingressRuleValues);
    }

    @Override
    public String toString() {
        return "{" +
                " host='" + getHost() + "'" +
                ", ingressRuleValues='" + getIngressRuleValues() + "'" +
                "}";
    }
}
