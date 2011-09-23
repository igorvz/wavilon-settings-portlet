package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.model.Agent;
import com.aimprosoft.wavilon.service.AgentDatabaseService;
import com.aimprosoft.wavilon.util.FormatUtil;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.ViewResults;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class AgentCouchDBServiceImpl extends AbstractCouchDBService implements AgentDatabaseService {

    public void addAgent(Agent agent) throws IOException {
        updateAgent(agent);
    }

    public Agent getAgent(String id) throws IOException {
        return (Agent) getModelById(id);
    }

    public List<Agent> getAllAgents() throws IOException {
        ViewResults viewResults = database.adhoc(functions.getAllAgentFunction());

        List<Agent> agentList = new LinkedList<Agent>();

        for (Document doc : viewResults.getResults()) {
            Agent agent = getAgent(doc.getId());

            agentList.add(agent);
        }

        return agentList;
    }

    public void removeAgent(Agent agent) throws IOException {
        removeModel(agent);
    }

    public void removeAgent(String id) throws IOException {
        removeModelById(id);
    }

    public void updateAgent(Agent agent) throws IOException {
        updateModel(agent);
    }

    public List<Agent> getAllAgentsByUser(Long userId, Long organizationId) throws IOException {
        String formattedFunction = FormatUtil.formatFunction(functions.getBaseModelsByUserAndTypeFunction(), "agent", userId, organizationId);

       ViewResults viewResults = database.adhoc(formattedFunction);

        List<Agent> agentList = new LinkedList<Agent>();

        for (Document doc : viewResults.getResults()) {
            Agent agent = getAgent(doc.getId());

            agentList.add(agent);
        }

        return agentList;
    }

}
