package com.aimprosoft.wavilon.model;

//todo entity for CouchDB
public class User extends BaseModel {
    private Long liferay_organization_id;
    private Long liferay_portal_id;
    private String revision;

    public User() {
    }

    public Long getLiferay_organization_id() {
        return liferay_organization_id;
    }

    public void setLiferay_organization_id(Long liferay_organization_id) {
        this.liferay_organization_id = liferay_organization_id;
    }

    public Long getLiferay_portal_id() {
        return liferay_portal_id;
    }

    public void setLiferay_portal_id(Long liferay_portal_id) {
        this.liferay_portal_id = liferay_portal_id;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }
}
