package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.model.Recording;
import com.aimprosoft.wavilon.service.RecordingDatabaseService;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.View;
import com.fourspaces.couchdb.ViewResults;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class RecordingCouchDBServiceImpl extends AbstractViewEntityService implements RecordingDatabaseService {
    @Autowired
    private CouchDBService couchDBService;

    @Autowired
    private ObjectMapper objectMapper;

    public CouchModel getModel(String id) throws IOException {
        return couchDBService.getModelById(id, true);
    }

    public void updateRecording(Recording recording, CouchModel model) throws IOException {
        Map<String, Object> properties = objectMapper.convertValue(recording, Map.class);
        updateCouchModel(model, properties);
    }


    public void addRecording(Recording recording, CouchModel model) throws IOException {
        updateRecording(recording, model);
    }

}
