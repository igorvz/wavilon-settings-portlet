package com.aimprosoft.wavilon.util;


import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Session;


//todo move to spring
public class DBProvider {
    private String databaseName;
    private Session session;

    public DBProvider(String databaseName) {
        this.databaseName = databaseName;
    }

    public Database getDatabase(){
        return session.getDatabase(databaseName);
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
