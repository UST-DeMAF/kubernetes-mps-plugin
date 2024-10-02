package ust.tad.kubernetesmpsplugin.models.tadm;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(
    scope = Component.class,
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "name")
public interface ComponentMixIn {}
