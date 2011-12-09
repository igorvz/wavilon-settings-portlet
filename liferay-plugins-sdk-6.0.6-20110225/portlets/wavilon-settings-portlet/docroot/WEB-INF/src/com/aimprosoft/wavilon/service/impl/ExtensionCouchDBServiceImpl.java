package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.model.Extension;
import com.aimprosoft.wavilon.service.ExtensionDatabaseService;
import org.codehaus.jackson.map.DeserializationConfig;
import org.ektorp.ViewQuery;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class ExtensionCouchDBServiceImpl extends AbstractViewEntityService implements ExtensionDatabaseService {

    public Extension getExtension(CouchModel model) throws IOException {
        return objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false).convertValue(model.getProperties(), Extension.class);
    }

    public void updateExtension(Extension extension, CouchModel model) throws IOException {
        Map<String, Object> properties = objectMapper.convertValue(extension, Map.class);
        updateCouchModel(model, properties);
    }


    public void addExtension(Extension extension, CouchModel model) throws IOException {
        updateExtension(extension, model);
    }


    public boolean checkCode(String modelId, Long organizationId, Integer code) throws IOException {

        ViewQuery query = new ViewQuery()
                .designDocId(functions.getDesignDocumentNodes())
                .viewName(functions.getExtensionCodeExist())
                .key("[" + organizationId + "," + code + "]");

        return 0 != connector.queryView(query, CouchModel.class).size() && !modelId.equals(connector.queryView(query, CouchModel.class).get(0).getId());

    }
}
