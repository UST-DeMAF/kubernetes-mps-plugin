package ust.tad.kubernetesmpsplugin.models.tadm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TechnologyAgnosticDeploymentModel {
    
    private UUID id = UUID.randomUUID();

    private UUID transformationProcessId;

    private List<Property> properties = new ArrayList<>();

    private List<Component> components = new ArrayList<>();

    private List<Relation> relations = new ArrayList<>();

    private List<ComponentType> componentTypes = new ArrayList<>();

    private List<RelationType> relationTypes = new ArrayList<>();


    public TechnologyAgnosticDeploymentModel() {
    }

    public TechnologyAgnosticDeploymentModel(UUID transformationProcessId, List<Property> properties, List<Component> components, List<Relation> relations, List<ComponentType> componentTypes, List<RelationType> relationTypes) {
        this.transformationProcessId = transformationProcessId;
        this.properties = properties;
        this.components = components;
        this.relations = relations;
        this.componentTypes = componentTypes;
        this.relationTypes = relationTypes;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTransformationProcessId() {
        return this.transformationProcessId;
    }

    public void setTransformationProcessId(UUID transformationProcessId) {
        this.transformationProcessId = transformationProcessId;
    }

    public List<Property> getProperties() {
        return this.properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public List<Component> getComponents() {
        return this.components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public List<Relation> getRelations() {
        return this.relations;
    }

    public void setRelations(List<Relation> relations) {
        this.relations = relations;
    }

    public List<ComponentType> getComponentTypes() {
        return this.componentTypes;
    }

    public void setComponentTypes(List<ComponentType> componentTypes) {
        this.componentTypes = componentTypes;
    }

    public List<RelationType> getRelationTypes() {
        return this.relationTypes;
    }

    public void setRelationTypes(List<RelationType> relationTypes) {
        this.relationTypes = relationTypes;
    }

    public TechnologyAgnosticDeploymentModel id(UUID id) {
        setId(id);
        return this;
    }

    public TechnologyAgnosticDeploymentModel transformationProcessId(UUID transformationProcessId) {
        setTransformationProcessId(transformationProcessId);
        return this;
    }

    public TechnologyAgnosticDeploymentModel properties(List<Property> properties) {
        setProperties(properties);
        return this;
    }

    public TechnologyAgnosticDeploymentModel components(List<Component> components) {
        setComponents(components);
        return this;
    }

    public TechnologyAgnosticDeploymentModel relations(List<Relation> relations) {
        setRelations(relations);
        return this;
    }

    public TechnologyAgnosticDeploymentModel componentTypes(List<ComponentType> componentTypes) {
        setComponentTypes(componentTypes);
        return this;
    }

    public TechnologyAgnosticDeploymentModel relationTypes(List<RelationType> relationTypes) {
        setRelationTypes(relationTypes);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof TechnologyAgnosticDeploymentModel)) {
            return false;
        }
        TechnologyAgnosticDeploymentModel technologyAgnosticDeploymentModel = (TechnologyAgnosticDeploymentModel) o;
        return Objects.equals(id, technologyAgnosticDeploymentModel.id) && Objects.equals(transformationProcessId, technologyAgnosticDeploymentModel.transformationProcessId) && Objects.equals(properties, technologyAgnosticDeploymentModel.properties) && Objects.equals(components, technologyAgnosticDeploymentModel.components) && Objects.equals(relations, technologyAgnosticDeploymentModel.relations) && Objects.equals(componentTypes, technologyAgnosticDeploymentModel.componentTypes) && Objects.equals(relationTypes, technologyAgnosticDeploymentModel.relationTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, transformationProcessId, properties, components, relations, componentTypes, relationTypes);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", transformationProcessId='" + getTransformationProcessId() + "'" +
            ", properties='" + getProperties() + "'" +
            ", components='" + getComponents() + "'" +
            ", relations='" + getRelations() + "'" +
            ", componentTypes='" + getComponentTypes() + "'" +
            ", relationTypes='" + getRelationTypes() + "'" +
            "}";
    }

    /**
     * Add new properties to the existing ones.
     * @param newProperties the properties to add.
     */
    public void addProperties(Collection<Property> newProperties) {
        List<Property> properties = this.getProperties();
        properties.addAll(newProperties);
        this.setProperties(properties);
    }

    /**
     * Add new components to the existing ones.
     * @param newComponents the components to add.
     */
    public void addComponents(Collection<Component> newComponents) {
        List<Component> components = this.getComponents();
        components.addAll(newComponents);
        this.setComponents(components);
    }

    /**
     * Add new relations to the existing ones.
     * @param newRelations the relations to add.
     */
    public void addRelations(Collection<Relation> newRelations) {
        List<Relation> relations = this.getRelations();
        relations.addAll(newRelations);
        this.setRelations(relations);
    }

    /**
     * Add new component types to the existing ones.
     * @param newComponentTypes the component types to add.
     */
    public void addComponentTypes(Collection<ComponentType> newComponentTypes) {
        List<ComponentType> componentTypes = this.getComponentTypes();
        componentTypes.addAll(newComponentTypes);
        this.setComponentTypes(componentTypes);
    }

    /**
     * Add new relation types to the existing ones.
     * @param newRelationTypes the relation types to add.
     */
    public void addRelationTypes(Collection<RelationType> newRelationTypes) {
        List<RelationType> relationTypes = this.getRelationTypes();
        relationTypes.addAll(newRelationTypes);
        this.setRelationTypes(relationTypes);
    }

    /**
     * Add all properties and model entities from another tadm to this tadm.
     * @param otherTADM the tadm from which to add properties and model entities.
     */
    public void addFromOtherTADM(TechnologyAgnosticDeploymentModel otherTADM) {
        this.addProperties(otherTADM.getProperties());
        this.addComponents(otherTADM.getComponents());
        this.addRelations(otherTADM.getRelations());
        this.addComponentTypes(otherTADM.getComponentTypes());
        this.addRelationTypes(otherTADM.getRelationTypes());
    }
    
}
