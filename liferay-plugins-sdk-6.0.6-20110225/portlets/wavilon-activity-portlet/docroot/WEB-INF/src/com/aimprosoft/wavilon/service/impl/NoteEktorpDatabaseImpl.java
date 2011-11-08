package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.model.Note;
import com.aimprosoft.wavilon.service.NoteDatabaseService;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class NoteEktorpDatabaseImpl extends AbstractViewEntityService implements NoteDatabaseService {

    @Autowired
    @Qualifier("pushtestDatabaseConnector")
    private CouchDbConnector connector;

    private Note getNote(String id) throws IOException {
        CouchModel model = getModel(id);
        return objectMapper.convertValue(model.getProperties(), Note.class);
    }

    @Override
    public Note getNote(CouchModel model) throws IOException {
        return objectMapper.convertValue(model.getProperties(), Note.class);
    }

    @Override
    public CouchModel getModel(String id) throws IOException {
        return connector.get(CouchModel.class, id);
    }

    @Override
    public List<CouchModel> getAllNote() throws IOException {
        ViewQuery query = new ViewQuery()
                .designDocId(functions.getPushTestDesignDocument())
                .viewName(functions.getFunctionPushTestAllNotes());

        return getCouchModelList(query);
    }

    @Override
    public void updateNote(Note note, CouchModel model) throws IOException {
        Map<String, Object> properties = objectMapper.convertValue(note, Map.class);
        model.setProperties(properties);

        connector.update(model);
    }

    @Override
    public List<CouchModel> getAllUsersCouchModelNote(Long organizationId) throws IOException {
        ViewQuery query = new ViewQuery()
                .designDocId(functions.getPushTestDesignDocument())
                .viewName(functions.getFunctionPushTestFilterNotes())
                .key(organizationId);

        return getCouchModelList(query);
    }

    @Override
    public void addNote(Note note, CouchModel model) throws IOException {
        Map<String, Object> properties = objectMapper.convertValue(note, Map.class);
        model.setProperties(properties);

        connector.create(model);
    }

    @Override
    public void removeNote(CouchModel model) throws IOException {
        connector.delete(model);
    }

    @Override
    public void removeNote(String id) throws IOException {
        connector.delete(getModel(id));
    }

    private List<CouchModel> getCouchModelList(ViewQuery query) {
        List<CouchModel> couchModelList = connector.queryView(query, CouchModel.class);

        if (0 == couchModelList.size()) {
            return new LinkedList<CouchModel>();
        } else {
            return couchModelList;
        }
    }
}
