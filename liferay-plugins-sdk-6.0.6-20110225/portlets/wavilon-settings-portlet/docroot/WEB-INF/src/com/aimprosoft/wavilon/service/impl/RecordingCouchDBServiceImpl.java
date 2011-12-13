package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.couch.Attachment;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.model.Recording;
import com.aimprosoft.wavilon.service.RecordingDatabaseService;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.View;
import com.fourspaces.couchdb.ViewResults;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.ektorp.AttachmentInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class RecordingCouchDBServiceImpl extends AbstractViewEntityService implements RecordingDatabaseService {

    public CouchModel getModel(String id) throws IOException {
        CouchModel couchModel = super.getModel(id);
//        final Recording recording = getModel(couchModel, Recording.class);
//
//        AttachmentInputStream attachmentInputStream = connector.getAttachment(id, recording.getFileName() + "." + recording.getFileType());
//        final Attachment attachment = new Attachment();
//        attachment.setData(IOUtils.toByteArray(attachmentInputStream));
//        attachment.setContentType(attachmentInputStream.getContentType());
//
//        couchModel.setAttachments(new HashMap<String, Attachment>(){{put(recording.getFileName(), attachment);}});

        return couchModel;
    }

    public void updateRecording(Recording recording, CouchModel model) throws IOException {
        Map<String, Object> properties = objectMapper.convertValue(recording, Map.class);
        updateCouchModel(model, properties);

    }


    public void addRecording(Recording recording, CouchModel model) throws IOException {
        updateRecording(recording, model);
    }

}
