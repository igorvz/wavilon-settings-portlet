package com.aimprosoft.wavilon.service;

import com.aimprosoft.wavilon.couch.CouchModelLite;

import java.io.IOException;
import java.util.List;

public interface CouchModelLiteDatabaseService {

    public CouchModelLite getCouchLiteModel(String id) throws IOException;

    public List<CouchModelLite> getAllCouchModelsLite(Long userId, Long organizationId, Object type) throws IOException;

}
