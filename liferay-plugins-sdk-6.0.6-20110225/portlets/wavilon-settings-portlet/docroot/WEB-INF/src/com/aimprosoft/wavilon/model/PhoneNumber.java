package com.aimprosoft.wavilon.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class PhoneNumber extends BaseModel {
    @JsonProperty("locator")
    private String locator;

    public PhoneNumber() {
    }

    public String getLocator() {
        return locator;
    }

    public void setLocator(String locator) {
        this.locator = locator;
    }
}
