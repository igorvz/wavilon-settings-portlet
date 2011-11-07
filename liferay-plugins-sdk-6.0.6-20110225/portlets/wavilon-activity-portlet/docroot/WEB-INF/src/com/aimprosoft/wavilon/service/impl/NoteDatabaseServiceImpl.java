package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.model.Note;
import com.aimprosoft.wavilon.service.NoteDatabaseService;
import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.ViewResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class NoteDatabaseServiceImpl extends AbstractViewEntityService implements NoteDatabaseService {

    private Note getNote(String id) throws IOException {
        CouchModel model = getModel(id);
        return objectMapper.convertValue(model.getProperties(), Note.class);
    }

    public Note getNote(CouchModel model) throws IOException {
        return objectMapper.convertValue(model.getProperties(), Note.class);
    }

    public CouchModel getModel(String id) throws IOException {
        return couchDBService.getModelById(id);
    }

    public List<CouchModel> getAllNote() throws IOException {
        ViewResults viewResults = database.getAllDocuments();
        List<CouchModel> noteList = new LinkedList<CouchModel>();

        for (Document doc : viewResults.getResults()) {

            CouchModel extension = getModel(doc.getId());

            noteList.add(extension);
        }
        return noteList;
    }

    public void updateNote(Note note, CouchModel model) throws IOException {
        Map<String, Object> properties = objectMapper.convertValue(note, Map.class);

        model.setProperties(properties);

        couchDBService.updateModel(model);
    }

    public List<CouchModel> getAllUsersCouchModelNote(Long organizationId) throws IOException {
//        View view = database.getDocument(functions.getDesignDocumentNodes()).getView(functions.getAllUniqueEntities());
//        view.setKey(urlEncoder.encode("[\"note\"," + organizationId + "]"));
//        ViewResults viewResults = database.view(view);
//
//        List<CouchModel> modelList = new LinkedList<CouchModel>();
//
//        for (Document doc : viewResults.getResults()) {
//
//            CouchModel model = getModel(doc.getId());
//
//            modelList.add(model);
//        }
//        return modelList;
        return null;
    }

    public void addNote(Note note, CouchModel model) throws IOException {
        updateNote(note, model);
    }

    public void removeNote(CouchModel model) throws IOException {
        couchDBService.removeModel(model);
    }

    public void removeNote(String id) throws IOException {
        couchDBService.removeModelById(id);
    }
}
