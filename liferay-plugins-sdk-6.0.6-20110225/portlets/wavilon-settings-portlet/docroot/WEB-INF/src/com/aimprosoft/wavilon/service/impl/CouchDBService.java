package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.couch.Attachment;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.model.Agent;
import com.aimprosoft.wavilon.service.SerializeService;
import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;
import net.sf.json.JSONObject;
import org.codehaus.jackson.map.ObjectReader;
import org.springframework.beans.factory.annotation.Required;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CouchDBService {
    private Database database;

    private ObjectReader objectReader;

    private ObjectReader objectToMapReader;

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
    public void setSerializeService(SerializeService<Agent> serializeService) {
        this.serializeService = serializeService;
    }

    @Required
    public void setObjectToMapReader(ObjectReader objectToMapReader) {
        this.objectToMapReader = objectToMapReader;
    }

    protected void addModel(CouchModel model) throws IOException {
        updateModel(model);
    }

    @SuppressWarnings("unchecked")
    protected void updateModel(Object model) throws IOException {
        Document document = serializeService.toDocument(model);
        database.saveDocument(document);
    }

    protected void removeModel(CouchModel model) throws IOException {
        removeModelById(model.getId());
    }

    protected void removeModelById(String id) throws IOException {
        Document document = database.getDocument(id);
        database.deleteDocument(document);
    }

    protected CouchModel getModelById(String documentId) throws IOException {
        return getModelById(documentId, false);
    }

     protected Map<String, Object> getMapById(String documentId) throws IOException {
        Document document = database.getDocument(documentId);
        return objectToMapReader.readValue(document.toString());
    }

    protected CouchModel getModelById(String documentId, boolean includeAttachments) throws IOException {
        Document document = database.getDocument(documentId);
        CouchModel model = objectReader.readValue(document.toString());

        if (includeAttachments) {
            Map<String, byte[]> attachmentsContent = getAttachmentsContent(document);

            mergeAttachments(model, attachmentsContent);
        }

        return model;
    }

    protected Map<String, byte[]> getAttachmentsContent(Document document) throws IOException {
        JSONObject attachmentsObject = document.getJSONObject("_attachments");

        if (attachmentsObject == null || attachmentsObject.size() == 0) {
            return Collections.emptyMap();
        }

        Iterator keysIterator = attachmentsObject.keys();

        Map<String, byte[]> attachmentsContent = new HashMap<String, byte[]>();
        while (keysIterator.hasNext()) {
            String attachmentName = String.valueOf(keysIterator.next());

            String attachment = database.getAttachment(document.getId(), attachmentName);

            attachmentsContent.put(attachmentName, getBytes(attachment));
        }

        return attachmentsContent;
    }

    /**
     * Returns raw String content.
     * It is necessary due content already has been decoded, and String#getBytes corrupts data.
     *
     * @param attachment - attachment
     * @return raw content
     */
    protected byte[] getBytes(String attachment) {
        byte[] raw = new byte[attachment.length()];
        attachment.getBytes(0, attachment.length(), raw, 0);
        return raw;
    }

    protected void mergeAttachments(CouchModel model, Map<String, byte[]> attachmentsContent) {
        //check if empty
//        if (attachmentsContent.isEmpty()) {
//            return;
//        }
//
//        Map<String, Attachment> attachments = model.getAttachments();
//        for (Map.Entry<String, Attachment> attachment : attachments.entrySet()) {
//            byte[] data = attachmentsContent.get(attachment.getKey());
//            attachment.getValue().setData(data);
//        }
    }

}
