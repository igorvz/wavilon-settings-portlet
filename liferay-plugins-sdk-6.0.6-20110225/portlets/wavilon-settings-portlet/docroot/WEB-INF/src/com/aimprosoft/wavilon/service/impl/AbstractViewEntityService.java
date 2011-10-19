package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.config.Functions;
import com.fourspaces.couchdb.Database;
import org.apache.catalina.util.URLEncoder;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class AbstractViewEntityService {
    @Autowired
    protected CouchDBService couchDBService;

    @Autowired
    protected Functions functions;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    @Qualifier("databaseNode")
    protected Database database;

    @Autowired
    protected URLEncoder urlEncoder;

}
