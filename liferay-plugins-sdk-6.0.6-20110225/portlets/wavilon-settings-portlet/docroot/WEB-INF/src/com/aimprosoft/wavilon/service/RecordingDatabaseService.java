package com.aimprosoft.wavilon.service;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.model.Recording;

import java.io.IOException;
import java.util.List;

public interface RecordingDatabaseService extends GeneralService {

    Recording getRecording(CouchModel model) throws IOException;

    List<Recording> getAllRecording() throws IOException;

    void updateRecording(Recording recording, CouchModel model) throws IOException;

    List<CouchModel> getAllUsersCouchModelToRecording(Long organizationId, boolean attachment) throws IOException;

    void addRecording(Recording recording, CouchModel model) throws IOException;

    void removeRecording(CouchModel model) throws IOException;

    void removeRecording(String id) throws IOException;
}
