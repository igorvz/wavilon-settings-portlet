package com.aimprosoft.wavilon.model;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(value = {"forwardTo"}, ignoreUnknown = true)
public class Recording extends BaseModel {

    @JsonProperty("forward_to")
    private String forwardTo;
    public Recording() {

    }

    public String getForwardTo() {
        return forwardTo;
    }

    public void setForwardTo(String forwardTo) {
        this.forwardTo = forwardTo;
    }
}
