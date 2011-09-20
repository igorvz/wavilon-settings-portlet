package com.aimprosoft.wavilon.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class Agent extends BaseModel {

    @JsonProperty("firstName")
    private String firstName;

    public Agent() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
