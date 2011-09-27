package com.aimprosoft.wavilon.service;

import com.aimprosoft.wavilon.model.Recording;

import java.io.IOException;
import java.util.List;

public interface RecordingDatabaseService {

    void addRecording(Recording recording) throws IOException;

    Recording getRecording(String id) throws IOException;

    List<Recording> getAllRecordings() throws IOException;

    List<Recording> getAllRecordingsByUserId(Long userId, Long organizationId) throws IOException;

    void removeRecording(Recording recording) throws IOException;

    void removeRecording(String id) throws IOException;

    void updateRecording(Recording recording) throws IOException;
}
