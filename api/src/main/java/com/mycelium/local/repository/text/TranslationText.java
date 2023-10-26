package com.mycelium.local.repository.text;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.model.naming.NamingStrategies;

@MappedEntity(namingStrategy = NamingStrategies.UpperCase.class)
public class TranslationText {
    @Id
    @GeneratedValue(GeneratedValue.Type.IDENTITY)
    public Long id;
    public String component;
    public String key;
    public String value;

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
