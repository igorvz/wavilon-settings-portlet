package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.model.Queue;
import com.aimprosoft.wavilon.service.QueueDatabaseService;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.ViewResults;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class QueueCouchDBServiceImpl extends AbstractCouchDBService implements QueueDatabaseService {

    public void addQueue(Queue queue) throws IOException {
        updateQueue(queue);
    }

    public Queue getQueue(String id) throws IOException {
        Document document = database.getDocument(id);
        return objectReader.readValue(document.toString());
    }

    public List<Queue> getAllQueues() throws IOException {
        ViewResults viewResults = database.adhoc(functions.getAllDocumentFunction());
        List<Queue> queueList = new LinkedList<Queue>();

        for (Document doc : viewResults.getResults()) {
            Queue queue = getQueue(doc.getId());

            queueList.add(queue);
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

    @SuppressWarnings("unchecked")
    public void updateQueue(Queue queue) throws IOException {
        Document document = serializeService.toDocument(queue);
        database.saveDocument(document);
    }

}
