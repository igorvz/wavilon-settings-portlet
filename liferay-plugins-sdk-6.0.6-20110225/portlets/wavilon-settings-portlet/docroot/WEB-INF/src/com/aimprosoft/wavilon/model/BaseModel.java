package com.aimprosoft.wavilon.model;

public abstract class BaseModel {
    private String id;
    private Long liferayUserId;
    private Long liferayOrganizationId;
    private String liferayPortalId;
    private String revision;

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
}
