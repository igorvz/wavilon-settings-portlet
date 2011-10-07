package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.model.Recording;
import com.aimprosoft.wavilon.service.RecordingDatabaseService;
import com.aimprosoft.wavilon.util.FormatUtil;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.ViewResults;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class RecordingCouchDBServiceImpl implements RecordingDatabaseService {
    @Autowired
    private CouchDBService couchDBService;

    @Autowired
    private ObjectMapper objectMapper;

    private Recording getRecording(String id) throws IOException {
        CouchModel model = getModel(id);
        return objectMapper.convertValue(model.getProperties(), Recording.class);
    }

    public Recording getRecording(CouchModel model) throws IOException {
        return objectMapper.convertValue(model.getProperties(), Recording.class);
    }

    public CouchModel getModel(String id) throws IOException {
        return couchDBService.getModelById(id, true);
    }

    public List<Recording> getAllRecording() throws IOException {
        ViewResults viewResults = couchDBService.database.adhoc(couchDBService.functions.getAllRecordingFunction());
        List<Recording> recordingList = new LinkedList<Recording>();

        for (Document doc : viewResults.getResults()) {

            Recording recording = getRecording(doc.getId());

            recordingList.add(recording);
        }
        return recordingList;
    }

    public void updateRecording(Recording recording, CouchModel model) throws IOException {
        Map<String, Object> properties = objectMapper.convertValue(recording, Map.class);

        model.setProperties(properties);

        couchDBService.updateModel(model);
    }

    public List<CouchModel> getAllUsersCouchModelToRecording(Long userId, Long organizationId) throws IOException {
        String formattedFunction = FormatUtil.formatFunction(couchDBService.functions.getBaseModelsByUserAndTypeFunction(), "recording", userId, organizationId);

        ViewResults viewResults = couchDBService.database.adhoc(formattedFunction);

        List<CouchModel> modelList = new LinkedList<CouchModel>();

        for (Document doc : viewResults.getResults()) {

            CouchModel model = getModel(doc.getId());

            modelList.add(model);
        }
        return modelList;
    }

    public void addRecording(Recording recording, CouchModel model) throws IOException {
        updateRecording(recording, model);
    }

    public void removeRecording(CouchModel model) throws IOException {
        couchDBService.removeModel(model);
    }

    public void removeRecording(String id) throws IOException {
        couchDBService.removeModelById(id);
    }
}
