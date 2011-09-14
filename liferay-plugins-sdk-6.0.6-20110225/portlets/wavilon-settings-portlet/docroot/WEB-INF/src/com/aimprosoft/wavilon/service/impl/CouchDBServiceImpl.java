package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.model.User;
import com.aimprosoft.wavilon.service.DatabaseService;
import com.aimprosoft.wavilon.util.DBUtil;
import com.aimprosoft.wavilon.util.MappingUtil;
import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.ViewResults;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class CouchDBServiceImpl implements DatabaseService {
    private Database database = DBUtil.getDatabase();


    public void addUser(User user) throws IOException {
        Document document = MappingUtil.toDocument(user);
        database.saveDocument(document);
    }

    public User getUser(String id) throws IOException {
        Document document = database.getDocument(id);
        return MappingUtil.toUser(document);
    }

    public List<User> getAllUsers() throws IOException {
        ViewResults viewResults = database.getAllDocuments();
        List<User> userList = new LinkedList<User>();

        for (Document doc : viewResults.getResults()) {
            String documentId = doc.getId();

            Document document = database.getDocument(documentId);

            userList.add(MappingUtil.toUser(document));
        }

        return userList;
    }

    public void removeUser(User user) throws IOException {
        Document document = database.getDocument(user.getId());
        database.deleteDocument(document);
    }

    public void removeUser(String id) throws IOException {
        Document document = database.getDocument(id);
        database.deleteDocument(document);
    }

    public void updateUser(User user) throws IOException {
        Document document = MappingUtil.toDocument(user);
        database.saveDocument(document);
    }
}
