package com.aimprosoft.wavilon.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class Extension extends BaseModel{
    @JsonProperty("firstName")
    private String firstName;

    public Extension(){
    }

    @Override
    public String getEntityType() {
        return "extension";
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty("extensionNumber")
    public int extensionNumber;

    public int getExtensionNumber() {
        return extensionNumber;
    }

    public void setExtensionNumber(int extensionNumber) {
        this.extensionNumber = extensionNumber;
    }
}
