package com.aimprosoft.wavilon.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class VirtualNumber extends BaseModel {
    @JsonProperty("locator")
    private String locator;

    public VirtualNumber() {
    }

    public String getLocator() {
        return locator;
    }

    public void setLocator(String locator) {
        this.locator = locator;
    }
}
