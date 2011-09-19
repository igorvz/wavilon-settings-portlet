package com.aimprosoft.wavilon.service;

import com.aimprosoft.wavilon.model.Queue;

import java.io.IOException;
import java.util.List;

//todo will be implemented
public interface QueueDatabaseService {

    void addQueue(Queue queue) throws IOException;

    Queue getQueue(String id) throws IOException;

    List<Queue> getAllQueues() throws IOException;

    void removeQueue(Queue queue) throws IOException;

    void removeQueue(String id) throws IOException;

    void updateQueue(Queue queue) throws IOException;
}
