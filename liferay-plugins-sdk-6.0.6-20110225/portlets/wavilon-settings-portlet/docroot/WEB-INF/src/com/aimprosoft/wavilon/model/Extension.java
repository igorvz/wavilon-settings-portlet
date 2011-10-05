package com.aimprosoft.wavilon.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class Extension extends BaseModel{

    @JsonProperty("extensionName")
    private String extensionName;

    @JsonProperty("extensionType")
    private String extensionType;

    @JsonProperty("extensionDestination")
    private String extensionDestination;

    public Extension(){
    }

    @Override
    public String getEntityType() {
        return "extension";
    }

    public String getExtensionName() {
        return extensionName;
    }

    public void setExtensionName(String extensionName) {
        this.extensionName = extensionName;
    }

    public String getExtensionType() {
        return extensionType;
    }

    public void setExtensionType(String extensionType) {
        this.extensionType = extensionType;
    }

    public String getExtensionDestination() {
        return extensionDestination;
    }

    public void setExtensionDestination(String extensionDestination) {
        this.extensionDestination = extensionDestination;
    }

    @Override
    public String toString() {
        return getExtensionName();
    }
}
