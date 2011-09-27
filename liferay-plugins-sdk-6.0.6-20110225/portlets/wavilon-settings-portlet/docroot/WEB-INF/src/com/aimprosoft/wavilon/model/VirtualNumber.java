package com.aimprosoft.wavilon.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class VirtualNumber extends BaseModel {

    @JsonProperty("name")
    private String name;

    public VirtualNumber() {
    }

    @Override
    public String getEntityType() {
        return "virtualNumber";
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
