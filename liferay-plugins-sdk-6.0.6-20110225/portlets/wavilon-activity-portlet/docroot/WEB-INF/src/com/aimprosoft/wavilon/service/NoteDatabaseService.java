package com.aimprosoft.wavilon.service;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.model.Note;
import org.ektorp.changes.DocumentChange;

import java.io.IOException;
import java.util.List;

public interface NoteDatabaseService {
    Note getNote(CouchModel model) throws IOException;

    Note getNote(String id) throws IOException;

    CouchModel getModel(String id) throws IOException;

    List<CouchModel> getAllNote() throws IOException;

    void updateNote(Note queue, CouchModel model) throws IOException;

    List<CouchModel> getAllUsersCouchModelNote(Long organizationId) throws IOException;

    void addNote(Note note, CouchModel model) throws IOException;

    void removeNote(CouchModel model) throws IOException;

    void removeNote(String id) throws IOException;

    List<DocumentChange> filterNodesChange(long seq);

    int getLastSeq();

    List<DocumentChange> getRemovedNodes(long seq);

}
