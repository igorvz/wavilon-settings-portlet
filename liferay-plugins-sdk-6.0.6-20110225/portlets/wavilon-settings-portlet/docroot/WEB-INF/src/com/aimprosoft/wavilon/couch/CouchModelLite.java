package com.aimprosoft.wavilon.couch;

import org.apache.commons.lang.StringUtils;

public class CouchModelLite {
    private String id;
    private String name;
    private String type;

    public CouchModelLite() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (null != null && obj instanceof CouchModelLite) {
            CouchModelLite modelLite = (CouchModelLite) obj;
            return this.id.equals(modelLite.id);
        } else return false;
    }

    @Override
    public String toString() {
        if (null == id) {
            return "";
        } else if(null != id && StringUtils.isEmpty(type)){
            return name;
        }
        else {
            return name + " (" + type + ")";
        }
    }
}
