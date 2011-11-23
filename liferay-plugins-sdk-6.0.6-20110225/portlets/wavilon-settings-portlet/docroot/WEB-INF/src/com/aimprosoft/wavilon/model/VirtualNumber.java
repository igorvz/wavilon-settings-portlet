package com.aimprosoft.wavilon.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(value = {"forwardTo", "recordCalls"}, ignoreUnknown = true)
public class VirtualNumber extends BaseModel {
    @JsonProperty("locator")
    private String locator;

    @JsonProperty("forward_to")
    private String forwardTo;

    @JsonProperty("record_calls")
    private Object recordCalls;

    public VirtualNumber() {
    }

    public String getLocator() {
        return locator;
    }

    public void setLocator(String locator) {
        this.locator = locator;
    }

    public String getForwardTo() {
        return forwardTo;
    }

    public void setForwardTo(String forwardTo) {
        this.forwardTo = forwardTo;
    }

    public Object getRecordCalls() {
        return recordCalls;
    }

    public void setRecordCalls(Object recordCalls) {
        this.recordCalls = recordCalls;
    }
}
