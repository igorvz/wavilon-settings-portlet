package com.aimprosoft.wavilon.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(value = {"attachedLiferayUserId"}, ignoreUnknown = true)
public class Agent extends  BaseModel{
    @JsonProperty("attached_liferay_user_id")
    private Long attachedLiferayUserId;

    public Agent() {
    }

    public Long getAttachedLiferayUserId() {
        return attachedLiferayUserId;
    }

    public void setAttachedLiferayUserId(Long attachedLiferayUserId) {
        this.attachedLiferayUserId = attachedLiferayUserId;
    }
}
