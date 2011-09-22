package com.aimprosoft.wavilon.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(value = {"id", "liferayUserId", "liferayOrganizationId", "liferayPortalId", "revision", "attachments"}, ignoreUnknown = true)
//PLEASE NOTE:
// 1. Annotation overrides in child classes
// 2. Do not ignore properties, where field name equals to JSON name
public abstract class BaseModel {

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

    @JsonProperty("entityType")
    public String getEntityType(){
        return getClass().getSimpleName();
    }

    @JsonProperty("type")
    public String getType(){
        return "wavilon-settings";
    }

    @JsonProperty("_attachments")
    private Map<String, Attachment> attachments;

    protected BaseModel() {
    }

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

    public Map<String, Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(Map<String, Attachment> attachments) {
        this.attachments = attachments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseModel baseModel = (BaseModel) o;

        if (!id.equals(baseModel.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
