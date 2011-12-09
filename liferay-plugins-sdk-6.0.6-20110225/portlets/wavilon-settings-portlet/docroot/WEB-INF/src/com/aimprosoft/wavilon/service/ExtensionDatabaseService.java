package com.aimprosoft.wavilon.service;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.model.Extension;

import java.io.IOException;

public interface ExtensionDatabaseService extends GeneralService {

    Extension getExtension(CouchModel model) throws IOException;

    void updateExtension(Extension extension, CouchModel model) throws IOException;

    void addExtension(Extension extension, CouchModel model) throws IOException;

    void removeModel(String id) throws IOException;

    public boolean checkCode(String modelId, Long organizationId, Integer code) throws IOException;

}
