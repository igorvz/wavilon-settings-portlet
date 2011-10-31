package com.aimprosoft.wavilon.service;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.model.Extension;

import java.io.IOException;
import java.util.List;

public interface ExtensionDatabaseService {

    Extension getExtension(CouchModel model) throws IOException;

    CouchModel getModel(String id) throws IOException;

    List<Extension> getAllExtension() throws IOException;

    void updateExtension(Extension extension, CouchModel model) throws IOException;

    List<CouchModel> getAllUsersCouchModelToExtension(Long organizationId) throws IOException;

    void addExtension(Extension extension, CouchModel model) throws IOException;

    void removeExtension(CouchModel model) throws IOException;

    void removeExtension(String id) throws IOException;

    public String getExtensionCode(Long organizationId, Integer code) throws IOException;

}
