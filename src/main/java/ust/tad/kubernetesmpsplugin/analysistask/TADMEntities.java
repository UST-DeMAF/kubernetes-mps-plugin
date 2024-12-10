package ust.tad.kubernetesmpsplugin.analysistask;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TADMEntities {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("entityType")
    private String entityType;

    @JsonProperty("entities")
    private List<String> entities = new ArrayList<>();

    public TADMEntities() {
    }

    public TADMEntities(String entityType, List<String> entities) {
        this.entityType = entityType;
        this.entities = entities;
    }

    public UUID getId() {
        return id;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public List<String> getEntities() {
        return entities;
    }

    public void setEntities(List<String> entities) {
        this.entities = entities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TADMEntities that = (TADMEntities) o;
        return Objects.equals(id, that.id) && Objects.equals(entityType, that.entityType) && Objects.equals(entities, that.entities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, entityType, entities);
    }

    @Override
    public String toString() {
        return "TADMEntities{" +
                "id=" + id +
                ", entityType='" + entityType + '\'' +
                ", entities=" + entities +
                '}';
    }
}
