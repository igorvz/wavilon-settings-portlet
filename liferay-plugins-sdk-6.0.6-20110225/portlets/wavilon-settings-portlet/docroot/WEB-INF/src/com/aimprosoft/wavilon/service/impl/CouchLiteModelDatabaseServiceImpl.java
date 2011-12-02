package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.service.CouchModelLiteDatabaseService;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.View;
import com.fourspaces.couchdb.ViewResults;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Service
public class CouchLiteModelDatabaseServiceImpl extends AbstractViewEntityService implements CouchModelLiteDatabaseService {

    public CouchModelLite getCouchLiteModel(String id) throws IOException {
        View view = database.getDocument(functions.getDesignDocumentNodes()).getView(functions.getCouchModelLiteName());
        view.setKey(urlEncoder.encode("\"" + id + "\""));
        ViewResults viewResults = database.view(view);

        return createCouchModelLite(id, viewResults.getResults().get(0).get("value"));
    }

    public List<CouchModelLite> getAllCouchModelsLite(Long organizationId, Object type) throws IOException {
        View view = database.getDocument(functions.getDesignDocumentNodes()).getView(functions.getAllCouchModelLite());
        view.setKey(urlEncoder.encode("[\"" + type + "\"," + organizationId + "]"));
        ViewResults viewResults = database.view(view);

        List<CouchModelLite> modelList = new LinkedList<CouchModelLite>();

        for (Document doc : viewResults.getResults()) {
            CouchModelLite modelLite = createCouchModelLite(doc.getId(), doc.get("value"));

            modelList.add(modelLite);
        }

        return modelList;
    }

    private CouchModelLite createCouchModelLite(String id, Object value) {
        List<String> values = objectMapper.convertValue(value, LinkedList.class);

        CouchModelLite modelLite = new CouchModelLite();
        modelLite.setId(id);
        modelLite.setName(values.get(0));
        modelLite.setType(values.get(1));
        return modelLite;
    }
}
