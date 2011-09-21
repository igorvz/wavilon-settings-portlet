package com.aimprosoft.wavilon.model;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.File;

public class Recording extends BaseModel{

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("img")
    private File img;

    public File getImg() {
        return img;
    }

    public void setImg(File img) {
        this.img = img;
    }

    public Recording() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
