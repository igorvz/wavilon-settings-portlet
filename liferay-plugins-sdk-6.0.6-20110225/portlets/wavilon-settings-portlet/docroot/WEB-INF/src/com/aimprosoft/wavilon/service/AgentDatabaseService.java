package com.aimprosoft.wavilon.service;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.model.Agent;

import java.io.IOException;
import java.util.List;

public interface AgentDatabaseService extends GeneralService {

    void updateAgent(Agent agent, CouchModel model, String extension) throws IOException;

    List<CouchModel> getAllUsersCouchModelAgent(Long organizationId) throws IOException;

    void addAgent(Agent agent, CouchModel model, String extension) throws IOException;

}
