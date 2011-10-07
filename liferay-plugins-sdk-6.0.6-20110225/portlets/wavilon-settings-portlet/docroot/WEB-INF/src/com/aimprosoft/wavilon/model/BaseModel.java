package com.aimprosoft.wavilon.model;

import org.codehaus.jackson.annotate.JsonProperty;

public abstract class BaseModel {
    @JsonProperty("name")
    private String name;

    protected BaseModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
