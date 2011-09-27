package com.aimprosoft.wavilon.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(value = {"id", "liferayUserId", "liferayOrganizationId", "liferayPortalId", "revision"}, ignoreUnknown = true)
public class ExtensionSipUrl extends BaseModel{

    @JsonProperty("sipURL")
    private String sipURL;

    @Override
    public String getEntityType() {
        return "extensionSipUrl";
    }

    public String getSipURL() {
        return sipURL;
    }

    public void setSipURL(String sipURL) {
        this.sipURL = sipURL;
    }
}
