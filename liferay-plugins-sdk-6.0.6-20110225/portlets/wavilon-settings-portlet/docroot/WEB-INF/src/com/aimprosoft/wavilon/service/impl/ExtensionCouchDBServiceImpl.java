package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.model.Extension;
import com.aimprosoft.wavilon.service.ExtensionDatabaseService;
import com.aimprosoft.wavilon.util.FormatUtil;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.ViewResults;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class ExtensionCouchDBServiceImpl implements ExtensionDatabaseService {
    @Autowired
    private CouchDBService couchDBService;

    @Autowired
    private ObjectMapper objectMapper;


    private Extension getExtension(String id) throws IOException {
        CouchModel model = getModel(id);
        return objectMapper.convertValue(model.getProperties(), Extension.class);
    }

    public Extension getExtension(CouchModel model) throws IOException {
        return objectMapper.convertValue(model.getProperties(), Extension.class);
    }

    public CouchModel getModel(String id) throws IOException {
        return couchDBService.getModelById(id);
    }

    public List<Extension> getAllExtension() throws IOException {

        ViewResults viewResults = couchDBService.database.adhoc(couchDBService.functions.getAllExtensionFunction());
        List<Extension> extensionList = new LinkedList<Extension>();

        for (Document doc : viewResults.getResults()) {

            Extension extension = getExtension(doc.getId());

            extensionList.add(extension);
        }
        return extensionList;
    }

    public void updateExtension(Extension extension, CouchModel model) throws IOException {
        Map<String, Object> properties = objectMapper.convertValue(extension, Map.class);

        model.setProperties(properties);

        couchDBService.updateModel(model);
    }

    public List<CouchModel> getAllUsersCouchModelToExtension(Long userId, Long organizationId) throws IOException {

        String formattedFunction = FormatUtil.formatFunction(couchDBService.functions.getBaseModelsByUserAndTypeFunction(), "extension", userId, organizationId);

        ViewResults viewResults = couchDBService.database.adhoc(formattedFunction);

        List<CouchModel> modelList = new LinkedList<CouchModel>();

        for (Document doc : viewResults.getResults()) {

            CouchModel model = getModel(doc.getId());

            modelList.add(model);
        }
        return modelList;
    }

    public void addExtension(Extension extension, CouchModel model) throws IOException {
        updateExtension(extension, model);
    }

    public void removeExtension(CouchModel model) throws IOException {
        couchDBService.removeModel(model);
    }

    public void removeExtension(String id) throws IOException {
        couchDBService.removeModelById(id);
    }
}
