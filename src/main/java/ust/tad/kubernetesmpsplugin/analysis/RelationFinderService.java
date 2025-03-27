package ust.tad.kubernetesmpsplugin.analysis;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.KubernetesDeploymentModel;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.common.types.StringStringMap;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.service.KubernetesService;
import ust.tad.kubernetesmpsplugin.kubernetesmodel.workload.deployment.KubernetesDeployment;
import ust.tad.kubernetesmpsplugin.models.tadm.*;

@Service
public class
RelationFinderService {

  private final String[] PROPERTY_KEYWORDS = {"connect", "host", "server", "url", "uri", "service", "addr", "endpoint"};

  private final Set<String> KUBERNETES_CLUSTER_TYPE_NAMES = Set.of("kubernetes_cluster",
          "azurerm_kubernetes_cluster", "aws_eks_cluster", "google_container_cluster");

  private RelationType connectsToRelationType = new RelationType();
  private RelationType hostedOnRelationType = new RelationType();

  /**
   * Creates EDMM relations for the newly created components.
   *
   * @param tadm the current technology-agnostic deployment model.
   * @param kubernetesDeploymentModel the Kubernetes deployment model in which to search for
   *     relations.
   * @param newComponents the newly created EDMM components from the transformation.
   * @return the newly created relations.
   */
  public Set<Relation> createRelations(
      TechnologyAgnosticDeploymentModel tadm,
      KubernetesDeploymentModel kubernetesDeploymentModel,
      List<Component> newComponents) {
    setRelationTypes(tadm.getRelationTypes());
    Set<Relation> newRelations = new HashSet<>();
    Map<KubernetesService, KubernetesDeployment> matchingServicesAndDeployments =
        findMatchingServicesAndDeployments(
            kubernetesDeploymentModel.getServices(), kubernetesDeploymentModel.getDeployments());
    for (Component newComponent : newComponents) {
      newRelations.addAll(
          findRelationsInProperties(tadm, newComponent, matchingServicesAndDeployments));
      Optional<Relation> relationToKubernetesCluster =
          createRelationToKubernetesCluster(tadm, newComponent);
      relationToKubernetesCluster.ifPresent(newRelations::add);
    }
    return newRelations;
  }

  /**
   * Iterates over all Kubernetes deployments and services and matches deployments where a selector
   * of the service matches the selector match label of a deployment.
   *
   * @param services the Kubernetes services.
   * @param deployments the Kubernetes deployments.
   * @return a Map with pairs of matching Kubernetes deployments and services.
   */
  private Map<KubernetesService, KubernetesDeployment> findMatchingServicesAndDeployments(
      Set<KubernetesService> services, Set<KubernetesDeployment> deployments) {
    Map<KubernetesService, KubernetesDeployment> matchingServicesAndDeployments = new HashMap<>();
    for (KubernetesDeployment deployment : deployments) {
      for (KubernetesService service : services) {
        for (StringStringMap selector : service.getSelectors()) {
          if (deployment.getSelectorMatchLabels().stream()
              .anyMatch(
                  selectorMatchLabel ->
                      selectorMatchLabel.getKey().equals(selector.getKey())
                          && selectorMatchLabel.getValue().equals(selector.getValue()))) {
            matchingServicesAndDeployments.put(service, deployment);
          }
        }
      }
    }
    return matchingServicesAndDeployments;
  }

  /**
   * Finds and creates an EDMM relation of type "hosted on" between a newly created component and an
   * already existing KubernetesCluster component.
   *
   * @param tadm
   * @param newComponent
   * @return
   */
  private Optional<Relation> createRelationToKubernetesCluster(
      TechnologyAgnosticDeploymentModel tadm, Component newComponent) {
    Optional<ComponentType> kubernetesClusterComponentTypeOpt =
        tadm.getComponentTypes().stream()
            .filter(componentType -> KUBERNETES_CLUSTER_TYPE_NAMES.contains(componentType.getName()))
            .findFirst();
    if (kubernetesClusterComponentTypeOpt.isPresent()) {
      Optional<Component> kubernetesClusterComponentOpt =
          tadm.getComponents().stream()
              .filter(
                  component -> component.getType().equals(kubernetesClusterComponentTypeOpt.get()))
              .findFirst();
      if (kubernetesClusterComponentOpt.isPresent()) {
        Relation relation = new Relation();
        relation.setType(this.hostedOnRelationType);
        relation.setName(
            newComponent.getName()
                + "_"
                + this.hostedOnRelationType.getName()
                + "_"
                + kubernetesClusterComponentOpt.get().getName());
        try {
          relation.setSource(newComponent);
          relation.setTarget(kubernetesClusterComponentOpt.get());
        } catch (InvalidRelationException e) {
          return Optional.empty();
        }
        relation.setConfidence(Confidence.SUSPECTED);
        return Optional.of(relation);
      }
    }
    return Optional.empty();
  }

  /**
   * Iterates over the properties of a component to find relations to other components. It uses the
   * PROPERTY_KEYWORDS to find properties that may contain references to other components. The
   * values of these properties are inspected, whether they contain the name of another component.
   * If so, a new relation between the source component and the matched component is created.
   *
   * @param tadm
   * @param sourceComponent
   * @param matchingServicesAndDeployments
   * @return the List of new relations that were created.
   */
  private List<Relation> findRelationsInProperties(
      TechnologyAgnosticDeploymentModel tadm,
      Component sourceComponent,
      Map<KubernetesService, KubernetesDeployment> matchingServicesAndDeployments) {
    List<Relation> newRelations = new ArrayList<>();
    List<String> targetComponentNames =
        tadm.getComponents().stream().map(ModelEntity::getName).collect(Collectors.toList());
    for (Property property : sourceComponent.getProperties()) {
      if (Arrays.stream(PROPERTY_KEYWORDS).anyMatch(property.getKey().toLowerCase()::contains)) {
        Optional<String> matchedComponentName =
            matchPropertyWithComponentNames(property, targetComponentNames);
        if (matchedComponentName.isPresent()
            && !matchedComponentName.get().equals(sourceComponent.getName())) {
          Optional<Relation> relationOpt =
              createRelationToComponent(
                  matchedComponentName.get(),
                  sourceComponent,
                  tadm.getComponents(),
                  matchingServicesAndDeployments);
          if (relationOpt.isPresent()) {
            Relation newRelation = relationOpt.get();
            if (!newRelations.stream().map(Relation::getName).collect(Collectors.toList())
                    .contains(newRelation.getName())) {
              newRelations.add(newRelation);
            }
          }
        }
      }
    }
    return newRelations;
  }

  /**
   * Analyzes a property whether it contains a reference to another component through the name of
   * the component. If a match is found the name of the matched component is returned. If there are
   * several matches, the longest match is chosen, as there may be component names embedded in the
   * names of other components (e.g., "my-service" and "my-service-db").
   *
   * @param property
   * @param componentNames
   * @return an Optional with the name of the matched component.
   */
  private Optional<String> matchPropertyWithComponentNames(
      Property property, List<String> componentNames) {
    List<String> matchedComponentNames =
        componentNames.stream()
            .filter(
                targetComponentName -> property.getValue().toString().contains(targetComponentName))
            .collect(Collectors.toList());
    if (matchedComponentNames.size() == 1) {
      return Optional.of(matchedComponentNames.get(0));
    } else if (matchedComponentNames.size() > 1) {
      return matchedComponentNames.stream().max(Comparator.comparingInt(String::length));
    } else {
      return Optional.empty();
    }
  }

  /**
   * Create an EDMM relation between a source and a target component of type "connects to". For
   * that, finds the target component by the given targetComponentName. If it cannot find the
   * component, it creates no relation.
   *
   * @param sourceComponent
   * @param components
   * @param matchingServicesAndDeployments
   * @return
   */
  private Optional<Relation> createRelationToComponent(
      String targetComponentName,
      Component sourceComponent,
      List<Component> components,
      Map<KubernetesService, KubernetesDeployment> matchingServicesAndDeployments) {
    Relation relation = new Relation();
    relation.setType(this.connectsToRelationType);
    relation.setConfidence(Confidence.CONFIRMED);
    try {
      relation.setSource(sourceComponent);
      Optional<Component> targetComponentOpt = getComponentByName(targetComponentName, components);
      if (targetComponentOpt.isPresent()) {
        relation.setTarget(targetComponentOpt.get());
        relation.setName(
            sourceComponent.getName()
                + "_"
                + this.connectsToRelationType.getName()
                + "_"
                + targetComponentOpt.get().getName());
        return Optional.of(relation);
      }
      targetComponentOpt =
          getComponentByMatchingService(
              targetComponentName, matchingServicesAndDeployments, components);
      if (targetComponentOpt.isPresent()) {
        relation.setTarget(targetComponentOpt.get());
        relation.setName(
            sourceComponent.getName()
                + "_"
                + this.connectsToRelationType.getName()
                + "_"
                + targetComponentOpt.get().getName());
        return Optional.of(relation);
      }
    } catch (InvalidRelationException e) {
      return Optional.empty();
    }
    return Optional.empty();
  }

  /**
   * Get a component by its name from a given List of components.
   *
   * @param name
   * @param components
   * @return
   */
  private Optional<Component> getComponentByName(String name, List<Component> components) {
    return components.stream().filter(component -> component.getName().equals(name)).findFirst();
  }

  /**
   * For a given serviceName, finds the EDMM component that was created based on this service.
   *
   * @param serviceName
   * @param matchingServicesAndDeployments
   * @param components
   * @return
   */
  private Optional<Component> getComponentByMatchingService(
      String serviceName,
      Map<KubernetesService, KubernetesDeployment> matchingServicesAndDeployments,
      List<Component> components) {
    Set<KubernetesService> services = matchingServicesAndDeployments.keySet();
    Optional<KubernetesService> matchedService =
        services.stream().filter(s -> s.getName().equals(serviceName)).findFirst();
    if (matchedService.isPresent()) {
      String componentName = matchingServicesAndDeployments.get(matchedService.get()).getName();
      Optional<Component> component =
          components.stream().filter(c -> c.getName().equals(componentName)).findFirst();
      if (component.isPresent()) {
        return component;
      }
    }
    return Optional.empty();
  }

  /**
   * Gets the "connects to" and "hosted on" relation types from the technology-agnostic deployment
   * model. Saves them in class-wide variables for reuse in newly created components.
   *
   * @param relationTypes
   */
  private void setRelationTypes(List<RelationType> relationTypes) {
    Optional<RelationType> connectsToRelationTypeOpt =
        relationTypes.stream()
            .filter(relationType -> relationType.getName().equals("ConnectsTo"))
            .findFirst();
    connectsToRelationTypeOpt.ifPresent(relationType -> this.connectsToRelationType = relationType);
    Optional<RelationType> hostedOnRelationTypeOpt =
        relationTypes.stream()
            .filter(relationType -> relationType.getName().equals("HostedOn"))
            .findFirst();
    hostedOnRelationTypeOpt.ifPresent(relationType -> this.hostedOnRelationType = relationType);
  }
}
