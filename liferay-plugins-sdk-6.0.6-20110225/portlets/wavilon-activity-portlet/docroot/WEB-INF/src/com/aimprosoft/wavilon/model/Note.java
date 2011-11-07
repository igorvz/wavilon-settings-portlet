package com.aimprosoft.wavilon.model;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;

public class Note {
    @JsonProperty("content")
    String content;

    @JsonProperty("name")
    String name;

    @JsonProperty("updateDate")
    Date updateDate;

    public Note() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
