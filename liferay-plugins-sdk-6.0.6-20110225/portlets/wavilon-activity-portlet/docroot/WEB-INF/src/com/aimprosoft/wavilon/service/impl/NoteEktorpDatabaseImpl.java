package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.model.Note;
import com.aimprosoft.wavilon.service.NoteDatabaseService;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.ViewQuery;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NoteEktorpDatabaseImpl extends AbstractViewEntityService implements NoteDatabaseService {

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
        HttpClient httpClient = null;
        CouchDbInstance couchDbInstance = null;
        CouchDbConnector connector = null;
        List<CouchModel> modelList = null;
        CouchModel model = null;
        try {
            httpClient = new StdHttpClient.Builder()
                    .url("http://ks3094659.kimsufi.com:5984/")
                    .build();

            couchDbInstance = new StdCouchDbInstance(httpClient);
            connector = couchDbInstance.createConnector("pushtest", true);

            model = connector.get(CouchModel.class, id);

        } catch (MalformedURLException e) {
        }


        return model;
    }

    @Override
    public List<CouchModel> getAllNote() throws IOException {
        HttpClient httpClient = null;
        CouchDbInstance couchDbInstance = null;
        CouchDbConnector connector = null;
        List<CouchModel> modelList = null;
        try {
            httpClient = new StdHttpClient.Builder()
                    .url("http://ks3094659.kimsufi.com:5984/")
                    .build();

            couchDbInstance = new StdCouchDbInstance(httpClient);
            connector = couchDbInstance.createConnector("pushtest", true);

            ViewQuery query = new ViewQuery()
                    .designDocId("_design/function")
                    .viewName("all");

            modelList = connector.queryView(query, CouchModel.class);

        } catch (MalformedURLException e) {
        }
        return modelList;
    }

    @Override
    public void updateNote(Note note, CouchModel model) throws IOException {

        Map<String, Object> out = new HashMap<String, Object>();
        out.put("note", note);

        model.setOutputs(out);

        HttpClient httpClient = null;
        CouchDbInstance couchDbInstance = null;
        CouchDbConnector connector = null;
        try {
            httpClient = new StdHttpClient.Builder()
                    .url("http://ks3094659.kimsufi.com:5984/")
                    .build();

            couchDbInstance = new StdCouchDbInstance(httpClient);
            connector = couchDbInstance.createConnector("pushtest", true);

            connector.update(model);

        } catch (MalformedURLException e) {
        }
    }

    @Override
    public List<CouchModel> getAllUsersCouchModelNote(Long organizationId) throws IOException {

        HttpClient httpClient = null;
        CouchDbInstance couchDbInstance = null;
        CouchDbConnector connector = null;
        List<CouchModel> modelList = null;
        try {
            httpClient = new StdHttpClient.Builder()
                    .url("http://ks3094659.kimsufi.com:5984/")
                    .build();

            couchDbInstance = new StdCouchDbInstance(httpClient);
            connector = couchDbInstance.createConnector("pushtest", true);

            ViewQuery query = new ViewQuery()
                    .designDocId("_design/function")
                    .viewName("filter")
                    .key(organizationId);

            modelList = connector.queryView(query, CouchModel.class);

        } catch (MalformedURLException e) {
        }
        return modelList;
    }

    @Override
    public void addNote(Note note, CouchModel model) throws IOException {
        Map<String, Object> out = new HashMap<String, Object>();
        out.put("note", note);

        model.setProperties(out);

        HttpClient httpClient = null;
        CouchDbInstance couchDbInstance = null;
        CouchDbConnector connector = null;
        try {
            httpClient = new StdHttpClient.Builder()
                    .url("http://ks3094659.kimsufi.com:5984/")
                    .build();

            couchDbInstance = new StdCouchDbInstance(httpClient);
            connector = couchDbInstance.createConnector("pushtest", true);

            connector.create(model);

        } catch (MalformedURLException e) {
        }


    }

    @Override
    public void removeNote(CouchModel model) throws IOException {
        HttpClient httpClient = null;
        CouchDbInstance couchDbInstance = null;
        CouchDbConnector connector = null;
        try {
            httpClient = new StdHttpClient.Builder()
                    .url("http://ks3094659.kimsufi.com:5984/")
                    .build();

            couchDbInstance = new StdCouchDbInstance(httpClient);
            connector = couchDbInstance.createConnector("pushtest", true);

            connector.delete(model);

        } catch (MalformedURLException e) {
        }
    }

    @Override
    public void removeNote(String id) throws IOException {

        HttpClient httpClient = null;
        CouchDbInstance couchDbInstance = null;
        CouchDbConnector connector = null;
        try {
            httpClient = new StdHttpClient.Builder()
                    .url("http://ks3094659.kimsufi.com:5984/")
                    .build();

            couchDbInstance = new StdCouchDbInstance(httpClient);
            connector = couchDbInstance.createConnector("pushtest", true);

            connector.delete(id);

        } catch (MalformedURLException e) {
        }
    }
}
