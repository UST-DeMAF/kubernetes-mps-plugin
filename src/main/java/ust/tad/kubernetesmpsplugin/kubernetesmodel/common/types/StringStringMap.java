package ust.tad.kubernetesmpsplugin.kubernetesmodel.common.types;

import java.util.Objects;

public class StringStringMap {
    
    private String key;

    private String value;


    public StringStringMap() {
    }

    public StringStringMap(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public StringStringMap key(String key) {
        setKey(key);
        return this;
    }

    public StringStringMap value(String value) {
        setValue(value);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof StringStringMap)) {
            return false;
        }
        StringStringMap label = (StringStringMap) o;
        return Objects.equals(key, label.key) && Objects.equals(value, label.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        return "{" +
            " key='" + getKey() + "'" +
            ", value='" + getValue() + "'" +
            "}";
    }


}
