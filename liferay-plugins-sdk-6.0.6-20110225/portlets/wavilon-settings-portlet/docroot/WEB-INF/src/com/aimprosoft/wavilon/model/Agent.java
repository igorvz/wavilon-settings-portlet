package com.aimprosoft.wavilon.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class Agent extends BaseModel {

    @JsonProperty("name")
    private String name;

   @JsonProperty("currentExtension")
    private String currentExtension;


    public Agent() {
    }

    @Override
    public String getEntityType() {
        return "agent";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrentExtension() {
        return currentExtension;
    }

    public void setCurrentExtension(String currentExtension) {
        this.currentExtension = currentExtension;
    }

    @Override
    public String toString() {
        return name;
    }

}
