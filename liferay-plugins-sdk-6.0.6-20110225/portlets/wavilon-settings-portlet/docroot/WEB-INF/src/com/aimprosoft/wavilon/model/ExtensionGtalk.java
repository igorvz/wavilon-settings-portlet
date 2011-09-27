package com.aimprosoft.wavilon.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(value = {"id", "liferayUserId", "liferayOrganizationId", "liferayPortalId", "revision"}, ignoreUnknown = true)
public class ExtensionGtalk extends BaseModel {

    @JsonProperty("extensionGtalk")
    private String gtalk;

    @Override
    public String getEntityType() {
        return "extensionGtalk";
    }

    public String getGtalk() {
        return gtalk;
    }

    public void setGtalk(String gtalk) {
        this.gtalk = gtalk;
    }
}
