package com.aimprosoft.wavilon.model;

public abstract class BaseModel {
    private String id;

    protected BaseModel(String id) {
        this.id = id;
    }

    protected BaseModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
