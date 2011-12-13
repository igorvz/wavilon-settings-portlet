package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.config.Functions;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchTypes;
import org.apache.catalina.util.URLEncoder;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractViewEntityService {
    @Autowired
    @Qualifier("nodesDBConnector")
    protected CouchDbConnector connector;

    @Autowired
    protected Functions functions;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected URLEncoder urlEncoder;

    protected void updateCouchModel(CouchModel model, Map<String, Object> properties) throws IOException {
        if (null == model.getRevision()) {
            model.setProperties(properties);
            connector.update(model);
        } else {
            String id = model.getId();

            if (null != model.getAttachments()) {
                String fileName = properties.get("file_name") + "." + properties.get("file_type");
                for (String attachmentId : getAttachments(id)) {
                    if (!attachmentId.equals(fileName)) {
                        model.removeAttachment(attachmentId);
                        connector.deleteAttachment(id, getLastRevision(id), attachmentId);
                    }
                }
                model.setRevision(getLastRevision(id));
            }


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


    private List<String> getAttachments(String docId) {
        ViewQuery query = (ViewQuery) new ViewQuery()
                .designDocId(functions.getDesignDocumentNodes())
                .viewName(functions.getAttachments())
                .key(docId);
        List<HashMap> mapList = connector.queryView(query, HashMap.class);
        if (0 == mapList.size()) {
            return Collections.emptyList();
        }
        HashMap hashMap = mapList.get(0);
        List<String> attachments = (List<String>) hashMap.get("attachments");
        return attachments;
    }

    private String getLastRevision(String docId) {
        ViewQuery query = (ViewQuery) new ViewQuery()
                .designDocId(functions.getDesignDocumentNodes())
                .viewName(functions.getLastRevision())
                .key(docId);
        List<HashMap> mapList = connector.queryView(query, HashMap.class);
        HashMap hashMap = mapList.get(0);
        return (String) hashMap.get("revision");
    }

}
