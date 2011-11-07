package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.couch.Attachment;
import com.aimprosoft.wavilon.model.Avatars;
import com.aimprosoft.wavilon.service.AvatarService;
import com.aimprosoft.wavilon.service.SerializeService;
import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;
import net.sf.json.JSONObject;
import org.codehaus.jackson.map.ObjectReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class AvatarServiceImpl implements AvatarService {

    @Autowired
    @Qualifier("databaseNode")
    private Database database;

    @Autowired
    private SerializeService serializeService;

    @Autowired
    private ObjectReader objectReader;

    public Map<String, Attachment> getAvatars(){
        try{
        Document document = database.getDocument("avatars");
        Avatars avatars = objectReader.readValue(document.toString());
        Map<String, byte[]> attachmentsContent = getAttachmentsContent(document);
        mergeAttachments(avatars, attachmentsContent);
            return avatars.getAttachments();
        }catch (Exception ignored) {
            return Collections.EMPTY_MAP;
        }
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

    protected byte[] getBytes(String attachment) {
        byte[] raw = new byte[attachment.length()];
        attachment.getBytes(0, attachment.length(), raw, 0);
        return raw;
    }

    protected void mergeAttachments(Avatars model, Map<String, byte[]> attachmentsContent) {
        //check if empty
        if (attachmentsContent.isEmpty()) {
            return;
        }

        Map<String, Attachment> attachments = model.getAttachments();
        for (Map.Entry<String, Attachment> attachment : attachments.entrySet()) {
            byte[] data = attachmentsContent.get(attachment.getKey());
            attachment.getValue().setData(data);
        }
    }


}
