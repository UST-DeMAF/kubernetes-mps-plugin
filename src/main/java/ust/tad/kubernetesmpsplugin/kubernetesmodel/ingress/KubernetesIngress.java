package ust.tad.kubernetesmpsplugin.kubernetesmodel.ingress;

import java.util.List;
import java.util.Objects;

public class KubernetesIngress {
    private String ingressClassName;
    private List<IngressRule> rules;

    public KubernetesIngress() {}

    public KubernetesIngress(String ingressClassName, List<IngressRule> rules) {
        this.ingressClassName = ingressClassName;
        this.rules = rules;
    }

    public String getIngressClassName() {
        return this.ingressClassName;
    }

    public void setIngressClassName(String ingressClassName) {
        this.ingressClassName = ingressClassName;
    }

    public List<IngressRule> getRules() {
        return this.rules;
    }

    public void setRules(List<IngressRule> rules) {
        this.rules = rules;
    }

    public KubernetesIngress ingressClasName(String ingressClassName) {
        this.ingressClassName = ingressClassName;
        return this;
    }

    public KubernetesIngress rules(List<IngressRule> rules) {
        this.rules = rules;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KubernetesIngress)) return false;

        KubernetesIngress ingress = (KubernetesIngress) o;
        return Objects.equals(this.ingressClassName, ingress.ingressClassName) &&
                Objects.equals(this.rules, ingress.rules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                this.ingressClassName,
                this.rules
        );
    }

    @Override
    public String toString() {
        return "{" +
                " ingressClassName='" + getIngressClassName() + "'" +
                ", rules='" + getRules() + "'" +
                "}";
    }
}
