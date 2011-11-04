package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.couch.PushModel;
import com.aimprosoft.wavilon.service.SerializeService;
import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;
import org.codehaus.jackson.map.ObjectReader;
import org.springframework.beans.factory.annotation.Required;

import java.io.IOException;


public class PushDBService {
    private Database database;

    private ObjectReader objectReader;

    protected SerializeService serializeService;

    @Required
    public void setDatabase(Database database) {
        this.database = database;
    }

    @Required
    public void setObjectReader(ObjectReader objectReader) {
        this.objectReader = objectReader;
    }

    @Required
    public void setSerializeService(SerializeService serializeService) {
        this.serializeService = serializeService;
    }

    protected void addModel(PushModel model) throws IOException {
        updateModel(model);
    }

    @SuppressWarnings("unchecked")
    protected void updateModel(PushModel model) throws IOException {
        Document document = serializeService.toDocument(model);
        database.saveDocument(document);
    }

    protected void removeModel(PushModel model) throws IOException {
        removeModelById(model.getId());
    }

    protected void removeModelById(String id) throws IOException {
        Document document = database.getDocument(id);
        database.deleteDocument(document);
    }

    protected PushModel getModelById(String documentId) throws IOException {
        return getModelById(documentId, false);
    }

    protected PushModel getModelById(String documentId, boolean includeAttachments) throws IOException {
        Document document = database.getDocument(documentId);
        PushModel model = objectReader.readValue(document.toString());

        return model;
    }
}
