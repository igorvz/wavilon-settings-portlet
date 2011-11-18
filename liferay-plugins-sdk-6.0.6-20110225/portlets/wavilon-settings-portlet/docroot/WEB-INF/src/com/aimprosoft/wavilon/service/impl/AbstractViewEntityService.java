package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.config.Functions;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.fourspaces.couchdb.Database;
import org.apache.catalina.util.URLEncoder;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.util.Map;

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


    protected void updateCouchModel(CouchModel model) throws IOException {
        if (null == model.getRevision()) {
            couchDBService.updateModel(model);
        } else {
            String id = model.getId();
            Map<String, Object> mergeMap = couchDBService.getMapById(id);
            Map<String, Object> modelForPutMap = objectMapper.convertValue(model, Map.class);
            mergeMap.putAll(modelForPutMap);
            couchDBService.updateModel(mergeMap);
        }
    }


}
