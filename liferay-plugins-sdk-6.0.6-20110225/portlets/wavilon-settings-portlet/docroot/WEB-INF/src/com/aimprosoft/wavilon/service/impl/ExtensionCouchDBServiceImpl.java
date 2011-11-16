package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.model.Extension;
import com.aimprosoft.wavilon.service.ExtensionDatabaseService;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.View;
import com.fourspaces.couchdb.ViewResults;
import org.springframework.stereotype.Service;
import sun.reflect.generics.tree.ReturnType;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class ExtensionCouchDBServiceImpl extends AbstractViewEntityService implements ExtensionDatabaseService {

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

        ViewResults viewResults = database.adhoc(functions.getAllUniqueEntities());
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

        if (null == model.getRevision()) {
            couchDBService.updateModel(model);
        } else {
            merge(model);
        }
    }




    public List<CouchModel> getAllUsersCouchModelToExtension(Long organizationId) throws IOException {

        View view = database.getDocument(functions.getDesignDocumentNodes()).getView(functions.getAllUniqueEntities());
        view.setKey(urlEncoder.encode("[\"extension\"," + organizationId + "]"));

        ViewResults viewResults = database.view(view);

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

    public boolean checkCode(String modelId, Long organizationId, Integer code) throws IOException {
        View view = database.getDocument(functions.getDesignDocumentNodes()).getView(functions.getExtensionCodeExist());
        view.setKey(urlEncoder.encode("[" + organizationId + "," + code + "]"));
        ViewResults viewResults = database.view(view);

        List<Document> documentList = viewResults.getResults();

        if (0 == documentList.size()) {
            return false;
        } else {
            Document document = documentList.get(0);
            return !modelId.equals(document.getId());
        }
    }
}
