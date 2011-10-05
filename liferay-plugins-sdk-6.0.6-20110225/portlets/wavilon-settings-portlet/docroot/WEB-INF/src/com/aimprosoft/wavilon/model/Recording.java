package com.aimprosoft.wavilon.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class Recording extends BaseModel {

    @JsonProperty("name")
    private String name;

    @JsonProperty("extensionId")
    private String extensionId;

    public Recording() {
    }

    @Override
    public String getEntityType() {
        return "recording";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtensionId() {
        return extensionId;
    }

    public void setExtensionId(String extensionId) {
        this.extensionId = extensionId;
    }
}
