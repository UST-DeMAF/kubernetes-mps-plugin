package ust.tad.kubernetesmpsplugin.models.tadm;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract MixIn Class for TechnologyAgnosticDeployment class that adds JsonProperty Annotations
 * to the setter for the component types and relation types fields.
 * This is used when importing the transformation result from the MPS transformation as it uses
 * other keys for those fields than the models service.
 */
public abstract class TechnologyAgnosticDeploymentModelMixIn {

    private List<ComponentType> componentTypes = new ArrayList<>();

    private List<RelationType> relationTypes = new ArrayList<>();

    @JsonProperty("component types")
    public void setComponentTypes(List<ComponentType> componentTypes) {
        this.componentTypes = componentTypes;
    }

    @JsonProperty("relation types")
    public void setRelationTypes(List<RelationType> relationTypes) {
        this.relationTypes = relationTypes;
    }
}
