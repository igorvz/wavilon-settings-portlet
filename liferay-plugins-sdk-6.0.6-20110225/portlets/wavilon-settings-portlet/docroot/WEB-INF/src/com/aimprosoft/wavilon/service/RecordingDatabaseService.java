package com.aimprosoft.wavilon.service;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.model.Recording;

import java.io.IOException;
import java.util.List;

public interface RecordingDatabaseService extends GeneralService {

    void updateRecording(Recording recording, CouchModel model) throws IOException;

    void addRecording(Recording recording, CouchModel model) throws IOException;
}
