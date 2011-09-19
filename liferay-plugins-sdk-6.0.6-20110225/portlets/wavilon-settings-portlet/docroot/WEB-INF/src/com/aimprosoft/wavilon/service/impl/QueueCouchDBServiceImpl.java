package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.model.Queue;
import com.aimprosoft.wavilon.service.QueueDatabaseService;
import com.aimprosoft.wavilon.util.MappingUtil;
import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.ViewResults;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class QueueCouchDBServiceImpl implements QueueDatabaseService {

    private Database database;

    public void addQueue(Queue queue) throws IOException {
        Document document = MappingUtil.toDocument(queue);
        database.saveDocument(document);
    }

    public Queue getQueue(String id) throws IOException {
        Document document = database.getDocument(id);
        return MappingUtil.toQueue(document);
    }

    public List<Queue> getAllQueues() throws IOException {
        ViewResults viewResults = database.getAllDocuments();
        List<Queue> queueList = new LinkedList<Queue>();

        for (Document doc : viewResults.getResults()) {
            String documentId = doc.getId();

            Document document = database.getDocument(documentId);

            queueList.add(MappingUtil.toQueue(document));
        }

        return queueList;
    }

    public void removeQueue(Queue queue) throws IOException {
        String documentId = queue.getId();
        Document document = database.getDocument(documentId);
        database.deleteDocument(document);
    }

    public void removeQueue(String id) throws IOException {
        Document document = database.getDocument(id);
        database.deleteDocument(document);
    }

    public void updateQueue(Queue queue) throws IOException {
        Document document = MappingUtil.toDocument(queue);
        database.saveDocument(document);
    }

    public void setDatabase(Database database) {
        this.database = database;
    }
}
