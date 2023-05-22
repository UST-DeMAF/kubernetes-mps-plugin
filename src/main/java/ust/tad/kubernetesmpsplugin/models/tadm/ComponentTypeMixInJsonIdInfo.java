package ust.tad.kubernetesmpsplugin.models.tadm;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(scope = ComponentType.class, generator = ObjectIdGenerators.PropertyGenerator.class, property="name")
public interface ComponentTypeMixInJsonIdInfo {
}
