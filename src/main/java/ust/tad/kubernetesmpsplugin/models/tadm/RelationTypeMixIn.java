package ust.tad.kubernetesmpsplugin.models.tadm;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(
    scope = RelationType.class,
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "name")
public abstract class RelationTypeMixIn {

  private RelationType parentType;

  @JsonProperty("extends")
  public void setParentType(RelationType parentType) {
    this.parentType = parentType;
  }
}
