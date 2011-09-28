package com.aimprosoft.wavilon.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class PhoneNumber extends BaseModel {

    @JsonProperty("name")
    private String name;

    @JsonProperty("number")
    private String number;

    public PhoneNumber() {
    }

    @Override
    public String getEntityType() {
        return "phoneNumber";
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
