package ust.tad.kubernetesmpsplugin.models.tadm;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(
    scope = ComponentType.class,
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "name")
public abstract class ComponentTypeMixIn {
  private ComponentType parentType;

  @JsonProperty("extends")
  public void setParentType(ComponentType parentType) {
    this.parentType = parentType;
  }
}
