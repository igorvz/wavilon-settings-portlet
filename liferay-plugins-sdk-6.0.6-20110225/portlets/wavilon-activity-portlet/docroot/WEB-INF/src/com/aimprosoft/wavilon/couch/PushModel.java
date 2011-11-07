package com.aimprosoft.wavilon.couch;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(value = {"id", "revision", "attachments"}, ignoreUnknown = true)
public class PushModel {
    @JsonProperty("_id")
    private String id;

    @JsonProperty("_rev")
    private String revision;

    @JsonProperty("type")
    private Object type;

    @JsonProperty("_attachments")
    private Map<String, Attachment> attachments;

    @JsonProperty("properties")
    private Map<String, Object> properties;

    public Map<String, Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(Map<String, Attachment> attachments) {
        this.attachments = attachments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public Object getType() {
        return type;
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

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof PushModel) {
            PushModel couchModel = (PushModel) obj;
            return this.id.equals(couchModel.id);
        } else return false;
    }
}
