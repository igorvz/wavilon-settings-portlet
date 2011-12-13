package com.aimprosoft.wavilon.couch;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.Attachment;
import org.ektorp.support.CouchDbDocument;

import java.util.Map;

@JsonIgnoreProperties(value = {"id", "liferayUserId", "liferayOrganizationId", "liferayPortalId", "revision", "attachments"}, ignoreUnknown = true)
//PLEASE NOTE:
// 1. Annotation overrides in child classes
// 2. Do not ignore properties, where field name equals to JSON name
public class CouchModel extends CouchDbDocument {
    @JsonProperty("liferay_user_id")
    private Long liferayUserId;

    @JsonProperty("liferay_organization_id")
    private Long liferayOrganizationId;

    @JsonProperty("liferay_portal_id")
    private String liferayPortalId;

    @JsonProperty("type")
    private Object type;

    @JsonProperty("properties")
    private Map<String, Object> properties;

    @JsonProperty("outputs")
    private Map<String, Object> outputs;

    public void setAttachment(Attachment attachment) {
        addInlineAttachment(attachment);
    }

    public String getId() {
        return super.getId();
    }

    public void setId(String id) {
        super.setId(id);
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
        return super.getRevision();
    }

    public void setRevision(String revision) {
        super.setRevision(revision);
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

    public void removeAttachment(String attachmentId) {
        super.removeAttachment(attachmentId);
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof CouchModel) {
            CouchModel couchModel = (CouchModel) obj;
            return this.getId().equals(couchModel.getId());
        } else return false;
    }

    @Override
    public String toString() {
        if (null != properties) {
            return (String) properties.get("name");
        } else return getId();
    }
}
