package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.model.Recording;
import com.aimprosoft.wavilon.service.RecordingDatabaseService;
import com.aimprosoft.wavilon.util.FormatUtil;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.ViewResults;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class RecordingCouchDBServiceImpl extends AbstractCouchDBService implements RecordingDatabaseService {

    public void addRecording(Recording recording) throws IOException {
        updateRecording(recording);
    }

    public Recording getRecording(String id) throws IOException {
        return (Recording) getModelById(id, true);
    }

    public List<Recording> getAllRecordings() throws IOException {
        ViewResults viewResults = database.adhoc(functions.getAllRecordingFunction());

        List<Recording> recordingList = new LinkedList<Recording>();

        for (Document doc : viewResults.getResults()) {
            Recording recording = getRecording(doc.getId());

            recordingList.add(recording);
        }

        return recordingList;
    }

    public List<Recording> getAllRecordingsByUserId(Long userId, Long organizationId) throws IOException {
        String formattedFunction = FormatUtil.formatFunction(functions.getBaseModelsByUserAndTypeFunction(), "recording", userId, organizationId);

        ViewResults viewResults = database.adhoc(formattedFunction);

        List<Recording> recordingList = new LinkedList<Recording>();

        for (Document doc : viewResults.getResults()) {

            Recording recording = getRecording(doc.getId());

            recordingList.add(recording);
        }
        return recordingList;
    }

    public void removeRecording(Recording recording) throws IOException {
        removeModel(recording);
    }

    public void removeRecording(String id) throws IOException {
        removeModelById(id);
    }

    @SuppressWarnings("unchecked")
    public void updateRecording(Recording recording) throws IOException {
        updateModel(recording);
    }
}
