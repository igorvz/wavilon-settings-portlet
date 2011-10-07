package com.aimprosoft.wavilon.couch;

public class CouchModelLite {
    private String id;
    private String name;

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
        return name;
    }
}
