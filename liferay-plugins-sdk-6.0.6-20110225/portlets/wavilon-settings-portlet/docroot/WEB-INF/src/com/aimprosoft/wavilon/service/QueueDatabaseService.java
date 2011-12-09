package com.aimprosoft.wavilon.service;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.model.Queue;

import java.io.IOException;
import java.util.List;

public interface QueueDatabaseService extends GeneralService {

    Queue getQueue(CouchModel model) throws IOException;

    void updateQueue(Queue queue, CouchModel model, List<String> agents) throws IOException;

    void addQueue(Queue queue, CouchModel model, List<String> agents) throws IOException;
}
