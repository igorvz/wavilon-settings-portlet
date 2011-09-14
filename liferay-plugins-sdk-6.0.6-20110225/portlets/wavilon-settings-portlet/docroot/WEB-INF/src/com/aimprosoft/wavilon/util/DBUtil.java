package com.aimprosoft.wavilon.util;

import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Session;

//todo move to spring
public class DBUtil {
    public static final String HOST = "localhost";
    public static final int PORT = 5984;
    public static final String USER = "admin";
    public static final String PASS = "admin";
    public static final String DB_NAME = "wavilon";

    private static Session session =  new Session(HOST, PORT, USER, PASS);

    public static Database getDatabase(){
        return session.getDatabase(DB_NAME);
    }




}
