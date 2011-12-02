package com.aimprosoft.wavilon.service;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.model.Agent;

import java.io.IOException;
import java.util.List;

public interface AgentDatabaseService extends GeneralService {

    Agent getAgent(CouchModel model) throws IOException;

    Agent getAgent(String id) throws IOException;

    List<Agent> getAllAgent() throws IOException;

    void updateAgent(Agent agent, CouchModel model, String extension) throws IOException;

    List<CouchModel> getAllUsersCouchModelAgent(Long organizationId) throws IOException;

    void addAgent(Agent agent, CouchModel model, String extension) throws IOException;

    void removeAgent(CouchModel model) throws IOException;

    void removeAgent(String id) throws IOException;
}
