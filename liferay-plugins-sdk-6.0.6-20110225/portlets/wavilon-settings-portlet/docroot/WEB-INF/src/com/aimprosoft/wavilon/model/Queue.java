package com.aimprosoft.wavilon.model;

import org.codehaus.jackson.annotate.JsonProperty;

//@JsonIgnoreProperties(value = {}, ignoreUnknown = true)
public class Queue extends BaseModel {

    @JsonProperty("title")
    private String title;

    public Queue() {
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}


