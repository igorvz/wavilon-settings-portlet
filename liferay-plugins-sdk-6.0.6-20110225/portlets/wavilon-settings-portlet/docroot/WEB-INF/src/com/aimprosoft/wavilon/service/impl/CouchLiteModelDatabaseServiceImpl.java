package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.service.CouchModelLiteDatabaseService;
import com.aimprosoft.wavilon.util.FormatUtil;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.ViewResults;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Service
public class CouchLiteModelDatabaseServiceImpl extends AbstractViewEntityService implements CouchModelLiteDatabaseService {

    public CouchModelLite getCouchLiteModel(String id) throws IOException {
        String formattedFunction = FormatUtil.formatFunction(functions.getCouchModelLiteName(), id);
        ViewResults viewResults = database.adhoc(formattedFunction);
        String modelLiteName = (String) viewResults.getResults().get(0).get("value");

        return createCouchModelLite(id, modelLiteName);
    }

    public List<CouchModelLite> getAllCouchModelsLite(Long organizationId, Object type) throws IOException {

        String formattedFunction = FormatUtil.formatFunction(functions.getAllCouchModelLite(), type, organizationId);

        ViewResults viewResults = database.adhoc(formattedFunction);

        List<CouchModelLite> modelList = new LinkedList<CouchModelLite>();

        for (Document doc : viewResults.getResults()) {
            CouchModelLite modelLite = createCouchModelLite(doc.getId(), (String) doc.get("value"));

            modelList.add(modelLite);
        }

        return modelList;
    }

    private CouchModelLite createCouchModelLite(String id, String name) {
        CouchModelLite modelLite = new CouchModelLite();
        modelLite.setId(id);
        modelLite.setName(name);
        return modelLite;
    }
}
