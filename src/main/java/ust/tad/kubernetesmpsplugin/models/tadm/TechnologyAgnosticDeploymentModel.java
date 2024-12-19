package ust.tad.kubernetesmpsplugin.models.tadm;

import java.util.*;
import java.util.stream.Collectors;

public class TechnologyAgnosticDeploymentModel {

  private UUID id = UUID.randomUUID();

  private UUID transformationProcessId;

  private List<Property> properties = new ArrayList<>();

  private List<Component> components = new ArrayList<>();

  private List<Relation> relations = new ArrayList<>();

  private List<ComponentType> componentTypes = new ArrayList<>();

  private List<RelationType> relationTypes = new ArrayList<>();

  public TechnologyAgnosticDeploymentModel() {}

  public TechnologyAgnosticDeploymentModel(
      UUID transformationProcessId,
      List<Property> properties,
      List<Component> components,
      List<Relation> relations,
      List<ComponentType> componentTypes,
      List<RelationType> relationTypes) {
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
    if (o == this) return true;
    if (!(o instanceof TechnologyAgnosticDeploymentModel)) {
      return false;
    }
    TechnologyAgnosticDeploymentModel technologyAgnosticDeploymentModel =
        (TechnologyAgnosticDeploymentModel) o;
    return Objects.equals(id, technologyAgnosticDeploymentModel.id)
        && Objects.equals(
            transformationProcessId, technologyAgnosticDeploymentModel.transformationProcessId)
        && Objects.equals(properties, technologyAgnosticDeploymentModel.properties)
        && Objects.equals(components, technologyAgnosticDeploymentModel.components)
        && Objects.equals(relations, technologyAgnosticDeploymentModel.relations)
        && Objects.equals(componentTypes, technologyAgnosticDeploymentModel.componentTypes)
        && Objects.equals(relationTypes, technologyAgnosticDeploymentModel.relationTypes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        id,
        transformationProcessId,
        properties,
        components,
        relations,
        componentTypes,
        relationTypes);
  }

  @Override
  public String toString() {
    return "{"
        + " id='"
        + getId()
        + "'"
        + ", transformationProcessId='"
        + getTransformationProcessId()
        + "'"
        + ", properties='"
        + getProperties()
        + "'"
        + ", components='"
        + getComponents()
        + "'"
        + ", relations='"
        + getRelations()
        + "'"
        + ", componentTypes='"
        + getComponentTypes()
        + "'"
        + ", relationTypes='"
        + getRelationTypes()
        + "'"
        + "}";
  }

  /**
   * Add new properties to the existing ones.
   *
   * @param newProperties the properties to add.
   */
  public void addProperties(Collection<Property> newProperties) {
    List<Property> properties = this.getProperties();
    properties.addAll(newProperties);
    this.setProperties(properties);
  }

  /**
   * Add new components to the existing ones.
   *
   * @param newComponents the components to add.
   */
  public void addComponents(Collection<Component> newComponents) {
    List<Component> components = this.getComponents();
    components.addAll(newComponents);
    this.setComponents(components);
  }

  /**
   * Add new relations to the existing ones.
   *
   * @param newRelations the relations to add.
   */
  public void addRelations(Collection<Relation> newRelations) {
    List<Relation> relations = this.getRelations();
    relations.addAll(newRelations);
    this.setRelations(relations);
  }

  /**
   * Add new ComponentTypes to the existing ComponentTypes.
   * If there already exists a ComponentType with the same name, the new ComponentType is not
   * added. Instead:
   * 1. Components using the new ComponentType are changed to use the existing one.
   * 2. Properties and Operations of the new ComponentType that are not present in the existing
   * ComponentType are added to the existing ComponentType.
   * 3. ComponentTypes that have the duplicate ComponentType set as the parentType are changed to
   * use the existing ComponentType.
   *
   * @param newComponentTypes the component types to add.
   */
  public void addComponentTypes(Collection<ComponentType> newComponentTypes) {
    List<ComponentType> componentTypes = this.getComponentTypes();
    List<String> componentTypeNames =
            componentTypes.stream().map(ComponentType::getName).collect(Collectors.toList());
    for (ComponentType newComponentType : newComponentTypes) {
      if (componentTypeNames.contains(newComponentType.getName())) {
        Optional<ComponentType> matchedComponentType =
                componentTypes.stream().filter(componentType -> componentType.getName().equals(newComponentType.getName())).findFirst();
        matchedComponentType.ifPresent(componentType -> {
          this.replaceComponentTypeForComponents(newComponentType, componentType);
          this.addPropertyToComponentTypeIfNotPresent(componentType, newComponentType);
          this.addOperationToComponentTypeIfNotPresent(componentType, newComponentType);
          this.replaceParentTypeForComponentTypes(newComponentType, componentType,
                  newComponentTypes);
          this.replaceParentTypeForComponentTypes(newComponentType, componentType, componentTypes);
        });
      } else {
        componentTypes.add(newComponentType);
      }
    }
    this.setComponentTypes(componentTypes);
  }

  /**
   * Add Properties from another ComponentType to an existing ComponentType if they are not
   * present.
   *
   * @param existingComponentType the ComponentType that Properties are added to.
   * @param otherComponentType the ComponentType the Properties are added from.
   */
  private void addPropertyToComponentTypeIfNotPresent(ComponentType existingComponentType, ComponentType otherComponentType) {
    List<Property> existingComponentTypeProperties = existingComponentType.getProperties();
    List<String> propertyKeys =
            existingComponentType.getProperties().stream().map(Property::getKey).collect(Collectors.toList());
    for (Property property: otherComponentType.getProperties()) {
      if (!propertyKeys.contains(property.getKey())) {
        existingComponentTypeProperties.add(property);
        existingComponentType.setProperties(existingComponentTypeProperties);
      }
    }
  }

  /**
   * Add Operations from another ComponentType to an existing ComponentType if they are not
   * present.
   *
   * @param existingComponentType the ComponentType that Operations are added to.
   * @param otherComponentType the ComponentType the Operations are added from.
   */
  private void addOperationToComponentTypeIfNotPresent(ComponentType existingComponentType, ComponentType otherComponentType) {
    List<Operation> existingComponentTypeOperations = existingComponentType.getOperations();
    List<String> operationNames =
            existingComponentType.getOperations().stream().map(Operation::getName).collect(Collectors.toList());
    for (Operation operation: otherComponentType.getOperations()) {
      if (!operationNames.contains(operation.getName())) {
        existingComponentTypeOperations.add(operation);
        existingComponentType.setOperations(existingComponentTypeOperations);
      }
    }
  }

  /**
   * Replace the ComponentType of a Component with a new one.
   *
   * @param oldComponentType the ComponentType to be replaced.
   * @param newComponentType the ComponentType that replaces the old one.
   */
  private void replaceComponentTypeForComponents(ComponentType oldComponentType, ComponentType newComponentType) {
    List<Component> components = this.getComponents();
    for (Component component : components) {
      if (component.getType().equals(oldComponentType)) {
        component.setType(newComponentType);
      }
    }
    this.setComponents(components);
  }

  /**
   * Replace the parentType of all ComponentTypes in a list of ComponentTypes that match the given
   * old ComponentTypes with a new ComponentTypes.
   *
   * @param oldComponentType the current parentType.
   * @param newComponentType the new parentType.
   * @param componentTypes the list of ComponentTypes to search for the old relation type.
   * @return
   */
  private Collection<ComponentType> replaceParentTypeForComponentTypes(
          ComponentType oldComponentType,
          ComponentType newComponentType,
          Collection<ComponentType> componentTypes) {
    return componentTypes.stream().filter(componentType -> (componentType.getParentType() != null &&
                    componentType.getParentType().equals(oldComponentType)))
            .peek(componentType -> componentType.setParentType(newComponentType))
            .collect(Collectors.toList());
  }

  /**
   * Add new relation types to the existing ones.
   * If there already exists a relation type with the same name, the new relation type is not
   * added and relations using the new relation type are changed to use the existing one.
   * In addition, relation types that have the duplicate relation type set as the parentType are
   * changed to use the existing relation type.
   *
   * @param newRelationTypes the relation types to add.
   */
  public void addRelationTypes(Collection<RelationType> newRelationTypes) {
    List<RelationType> relationTypes = this.getRelationTypes();
    List<String> relationTypeNames =
            relationTypes.stream().map(RelationType::getName).collect(Collectors.toList());
    for (RelationType otherRelationType : newRelationTypes) {
      if (relationTypeNames.contains(otherRelationType.getName())) {
        Optional<RelationType> matchedRelationType =
                relationTypes.stream().filter(relationType -> relationType.getName().equals(otherRelationType.getName())).findFirst();
        matchedRelationType.ifPresent(relationType -> {
          this.replaceRelationTypeForRelations(otherRelationType, relationType);
          this.replaceParentTypeForRelationTypes(otherRelationType, relationType, newRelationTypes);
          this.replaceParentTypeForRelationTypes(otherRelationType, relationType, relationTypes);
        });
      } else {
        relationTypes.add(otherRelationType);
      }
    }
    this.setRelationTypes(relationTypes);
  }

  /**
   * Replace the RelationType of a Relation with a new one.
   *
   * @param oldRelationType the relation type to be replaced.
   * @param newRelationType the relation type that replaces the old one.
   */
  private void replaceRelationTypeForRelations(RelationType oldRelationType, RelationType newRelationType) {
    List<Relation> relations = this.getRelations();
    for (Relation relation : relations) {
      if (relation.getType().equals(oldRelationType)) {
        relation.setType(newRelationType);
      }
    }
    this.setRelations(relations);
  }

  /**
   * Replace the parentType of all RelationTypes in a list of RelationTypes that match the given
   * old RelationType with a new RelationType.
   *
   * @param oldRelationType the current parentType.
   * @param newRelationType the new parentType.
   * @param relationTypes the list of RelationTypes to search for the old relation type.
   * @return
   */
  private Collection<RelationType> replaceParentTypeForRelationTypes(
          RelationType oldRelationType,
          RelationType newRelationType,
          Collection<RelationType> relationTypes) {
    return relationTypes.stream().filter(relationType -> (relationType.getParentType() != null &&
                    relationType.getParentType().equals(oldRelationType)))
            .peek(relationType -> relationType.setParentType(newRelationType))
            .collect(Collectors.toList());
  }

  /**
   * Add all properties and model entities from another tadm to this tadm.
   *
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
