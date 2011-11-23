package com.aimprosoft.wavilon.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(value = {"recordCalls"}, ignoreUnknown = true)
public class PhoneNumber extends BaseModel {
    @JsonProperty("locator")
    private String locator;

    @JsonProperty("record_calls")
    private Object recordCalls;

    public PhoneNumber() {
    }

    public String getLocator() {
        return locator;
    }

    public void setLocator(String locator) {
        this.locator = locator;
    }

    public Object getRecordCalls() {
        return recordCalls;
    }

    public void setRecordCalls(Object record_calls) {
        this.recordCalls = record_calls;
    }
}
