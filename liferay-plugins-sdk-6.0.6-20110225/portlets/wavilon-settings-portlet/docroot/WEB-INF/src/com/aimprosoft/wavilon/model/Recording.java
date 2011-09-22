package com.aimprosoft.wavilon.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class Recording extends BaseModel{

    @JsonProperty("firstName")
    private String firstName;

    public Recording() {
    }

    @Override
    public String getEntityType() {
        return "recording";
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
