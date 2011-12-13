package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.service.CouchModelLiteDatabaseService;
import org.ektorp.ViewQuery;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CouchLiteModelDatabaseServiceImpl extends AbstractViewEntityService implements CouchModelLiteDatabaseService {

    public CouchModelLite getCouchLiteModel(String id) throws IOException {
        ViewQuery query = new ViewQuery()
                .designDocId(functions.getDesignDocumentNodes())
                .viewName(functions.getCouchModelLiteName())
                .key(id);
        return connector.queryView(query, CouchModelLite.class).get(0);
    }

    public List<CouchModelLite> getAllCouchModelsLite(Long organizationId, Object type) throws IOException {
        ViewQuery query = new ViewQuery()
                .designDocId(functions.getDesignDocumentNodes())
                .viewName(functions.getAllCouchModelLite())
                .key("[\"" + type.toString() + "\"," + organizationId + "]");
        return connector.queryView(query, CouchModelLite.class);

    }
}
