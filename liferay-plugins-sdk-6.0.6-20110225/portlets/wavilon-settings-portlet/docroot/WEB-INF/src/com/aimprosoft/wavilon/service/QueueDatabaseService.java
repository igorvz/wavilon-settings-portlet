package com.aimprosoft.wavilon.service;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.model.Queue;

import java.io.IOException;
import java.util.List;

public interface QueueDatabaseService {

    Queue getQueue(CouchModel model) throws IOException;

    CouchModel getModel(String id) throws IOException;

    List<Queue> getAllQueue() throws IOException;

    void updateQueue(Queue queue, CouchModel model, List<String> agents) throws IOException;

    List<CouchModel> getAllUsersCouchModelQueue(Long userId, Long organizationId) throws IOException;

    void addQueue(Queue queue, CouchModel model, List<String> agents) throws IOException;

    void removeQueue(CouchModel model) throws IOException;

    void removeQueue(String id) throws IOException;
}
