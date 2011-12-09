package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.config.Functions;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.fourspaces.couchdb.Database;
import org.apache.catalina.util.URLEncoder;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.ektorp.AttachmentInputStream;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractViewEntityService {
//    @Autowired
//    protected CouchDBService couchDBService;

    @Autowired
    @Qualifier("nodesDBConnector")
    protected CouchDbConnector connector;

    @Autowired
    protected Functions functions;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    @Qualifier("databaseNode")
    protected Database database;

    @Autowired
    protected URLEncoder urlEncoder;

    protected void updateCouchModel(CouchModel model, Map<String, Object> properties) throws IOException {
        if (null == model.getRevision()) {
            model.setProperties(properties);
            connector.update(model);
        } else {
            String id = model.getId();
            Map<String, Object> mergeMap = connector.get(HashMap.class, id);
            Map<String, Object> mergePropertiesMap = (Map<String, Object>) mergeMap.get("properties");
            Map<String, Object> modelForPutMap = objectMapper.convertValue(model, Map.class);

            mergePropertiesMap.putAll(properties);
            mergeMap.putAll(modelForPutMap);

            mergeMap.put("properties", mergePropertiesMap);

            connector.update(mergeMap);
        }
    }

    public CouchModel getModel(String id) throws IOException {
        return connector.get(CouchModel.class, id);
    }


    public <T> T getModel(CouchModel model, Class<T> modelClass) throws IOException {
        return objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false).convertValue(model.getProperties(), modelClass);
    }

    public List<CouchModel> getAvailableCouchModels(Long organizationId, CouchTypes type) throws IOException {
        ViewQuery query = new ViewQuery()
                .designDocId(functions.getDesignDocumentNodes())
                .viewName(functions.getAllUniqueEntities())
                .key("[\"" + type.toString() + "\"," + organizationId + "]");
        return connector.queryView(query, CouchModel.class);
    }

    public void removeModel(String id) {
        try {
            connector.delete(getModel(id));
        } catch (IOException ignored) {
        }
    }

}
