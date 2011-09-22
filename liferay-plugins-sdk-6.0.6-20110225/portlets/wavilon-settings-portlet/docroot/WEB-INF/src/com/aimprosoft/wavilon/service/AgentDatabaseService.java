package com.aimprosoft.wavilon.service;

import com.aimprosoft.wavilon.model.Agent;

import java.io.IOException;
import java.util.List;

public interface AgentDatabaseService {

    void addAgent(Agent agent) throws IOException;

    Agent getAgent(String id) throws IOException;

    List<Agent> getAllAgents() throws IOException;

    void removeAgent(Agent agent) throws IOException;

    void removeAgent(String id) throws IOException;

    void updateAgent(Agent agent) throws IOException;

}
