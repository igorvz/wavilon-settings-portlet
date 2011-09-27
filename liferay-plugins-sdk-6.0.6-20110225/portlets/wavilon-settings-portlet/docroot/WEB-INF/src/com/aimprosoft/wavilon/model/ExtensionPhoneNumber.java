package com.aimprosoft.wavilon.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(value = {"id", "liferayUserId", "liferayOrganizationId", "liferayPortalId", "revision"}, ignoreUnknown = true)
public class ExtensionPhoneNumber extends BaseModel{

    @JsonProperty("extensionPhoneNumber")
    private String phoneNumber;

    @Override
    public String getEntityType() {
        return "extensionPhoneNumber";
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

