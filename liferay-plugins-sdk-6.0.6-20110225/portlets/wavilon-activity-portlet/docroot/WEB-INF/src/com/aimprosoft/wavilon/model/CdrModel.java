package com.aimprosoft.wavilon.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(value = {"id", "liferayUserId", "liferayOrganizationId", "liferayPortalId", "revision", "attachments"}, ignoreUnknown = true)
public class CdrModel{

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

    @JsonProperty("duration")
    private double duration;

    @JsonProperty("recorded")
    private boolean recorded;

    @JsonProperty("uniqueid")
    private String uniqueid;

    @JsonProperty("clidnum")
    private int clidnum;

    @JsonProperty("calldate")
    private String calldate;

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
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public boolean isRecorded() {
        return recorded;
    }

    public void setRecorded(boolean recorded) {
        this.recorded = recorded;
    }

    public String getUniqueid() {
        return uniqueid;
    }

    public void setUniqueid(String uniqueid) {
        this.uniqueid = uniqueid;
    }

    public int getClidnum() {
        return clidnum;
    }

    public void setClidnum(int clidnum) {
        this.clidnum = clidnum;
    }

    public String getCalldate() {
        return calldate;
    }

    public void setCalldate(String calldate) {
        this.calldate = calldate;
    }

     @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof CdrModel) {
            CdrModel cdrModel = (CdrModel) obj;
            return this.id.equals(cdrModel.id);
        } else return false;
    }

    @Override
    public String toString() {
        if (null != type) {
            return (String) type;
        } else return id;
    }
}
