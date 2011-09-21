package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.model.Recording;
import com.aimprosoft.wavilon.service.RecordingDatabaseService;
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
        Document document = database.getDocument(id);
        return objectReader.readValue(document.toString());
    }

    public List<Recording> getAllRecordings() throws IOException {
        ViewResults viewResults = database.adhoc(functions.getAllDocumentFunction());

        List<Recording> recordingList = new LinkedList<Recording>();

        for (Document doc : viewResults.getResults()) {
            Recording recording = getRecording(doc.getId());

            recordingList.add(recording);
        }

        return recordingList;
    }

    public void removeRecording(Recording recording) throws IOException {
        String documentId = recording.getId();
        Document document = database.getDocument(documentId);
        database.deleteDocument(document);
    }

    public void removeRecording(String id) throws IOException {
        Document document = database.getDocument(id);
        database.deleteDocument(document);
    }

    @SuppressWarnings("unchecked")
    public void updateRecording(Recording recording) throws IOException {
      Document document = serializeService.toDocument(recording);
        database.saveDocument(document);
    }
}
