package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.config.Functions;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.View;
import com.fourspaces.couchdb.ViewResults;
import org.apache.catalina.util.URLEncoder;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
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

      protected void updateCouchModel(CouchModel model, Map<String, Object> properties) throws IOException {
        if (null == model.getRevision()) {
            model.setProperties(properties);
            couchDBService.updateModel(model);
        } else {
            String id = model.getId();
            Map<String, Object> mergeMap = couchDBService.getMapById(id);
            Map<String, Object> mergePropertiesMap = (Map<String, Object>) mergeMap.get("properties");
            Map<String, Object> modelForPutMap = objectMapper.convertValue(model, Map.class);

            mergePropertiesMap.putAll(properties);
            mergeMap.putAll(modelForPutMap);

            mergeMap.put("properties", mergePropertiesMap);

            couchDBService.updateModel(mergeMap);
        }
    }

    public CouchModel getModel(String id) throws IOException {
        return couchDBService.getModelById(id);
    }

    public <T> T  getModel(CouchModel model, Class<T> modelClass) throws IOException{
        return objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false).convertValue(model.getProperties(), modelClass);
    }

    public List<CouchModel> getAvailableCouchModels(Long organizationId, CouchTypes type) throws IOException {
        View view = database.getDocument(functions.getDesignDocumentNodes()).getView(functions.getAllUniqueEntities());
        view.setKey(urlEncoder.encode("[\"" + type.toString() + "\"," + organizationId + "]"));
        ViewResults viewResults = database.view(view);

        List<CouchModel> modelList = new LinkedList<CouchModel>();

        for (Document doc : viewResults.getResults()) {

            CouchModel model = getModel(doc.getId());

            modelList.add(model);
        }
        return modelList;
    }
}
