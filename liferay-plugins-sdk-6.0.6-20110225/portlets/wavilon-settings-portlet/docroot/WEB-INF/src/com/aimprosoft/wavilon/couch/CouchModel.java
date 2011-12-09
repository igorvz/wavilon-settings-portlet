package com.aimprosoft.wavilon.couch;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(value = {"id", "liferayUserId", "liferayOrganizationId", "liferayPortalId", "revision", "attachments"}, ignoreUnknown = true)
//PLEASE NOTE:
// 1. Annotation overrides in child classes
// 2. Do not ignore properties, where field name equals to JSON name
public class CouchModel {
    @JsonProperty("_id")
    private String id;

    @JsonProperty("liferay_user_id")
    private Long liferayUserId;

    @JsonProperty("liferay_organization_id")
    private Long liferayOrganizationId;

    @JsonProperty("liferay_portal_id")
    private String liferayPortalId;

    @JsonProperty("_rev")
    private String revision;

    @JsonProperty("type")
    private Object type;

    @JsonProperty("properties")
    private Map<String, Object> properties;

    @JsonProperty("outputs")
    private Map<String, Object> outputs;


    @JsonProperty("_attachments")
    private Map<String, Attachment> attachments;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getLiferayUserId() {
        return liferayUserId;
    }

    public void setLiferayUserId(Long liferayUserId) {
        this.liferayUserId = liferayUserId;
    }

    public Long getLiferayOrganizationId() {
        return liferayOrganizationId;
    }

    public void setLiferayOrganizationId(Long liferayOrganizationId) {
        this.liferayOrganizationId = liferayOrganizationId;
    }

    public String getLiferayPortalId() {
        return liferayPortalId;
    }

    public void setLiferayPortalId(String liferayPortalId) {
        this.liferayPortalId = liferayPortalId;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public Object getType() {
        return type.toString();
    }

    public void setType(Object type) {
        this.type = type;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public Map<String, Object> getOutputs() {
        return outputs;
    }

    public void setOutputs(Map<String, Object> outputs) {
        this.outputs = outputs;
    }

    public Map<String, Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(Map<String, Attachment> attachments) {
        this.attachments = attachments;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof CouchModel) {
            CouchModel couchModel = (CouchModel) obj;
            return this.id.equals(couchModel.id);
        } else return false;
    }

    @Override
    public String toString() {
        if (null != properties) {
            return (String) properties.get("name");
        } else return id;
    }
}
