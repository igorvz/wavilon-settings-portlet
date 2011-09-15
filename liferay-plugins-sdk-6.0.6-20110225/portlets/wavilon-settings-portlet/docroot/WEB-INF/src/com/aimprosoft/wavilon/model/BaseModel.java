package com.aimprosoft.wavilon.model;

public abstract class BaseModel {
    private Long liferay_user_id;

    protected BaseModel() {
    }

    public Long getLiferay_user_id() {
        return liferay_user_id;
    }

    public void setLiferay_user_id(Long liferay_user_id) {
        this.liferay_user_id = liferay_user_id;
    }
}
