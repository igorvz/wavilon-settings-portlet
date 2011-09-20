package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.model.Agent;
import com.aimprosoft.wavilon.service.AgentDatabaseService;
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
        Document document = database.getDocument(id);
        return objectReader.readValue(document.toString());
    }

    public List<Agent> getAllAgents() throws IOException {
        ViewResults viewResults = database.adhoc(functions.getAllDocumentFunction());

        List<Agent> agentList = new LinkedList<Agent>();

        for (Document doc : viewResults.getResults()) {
            Agent agent = getAgent(doc.getId());

            agentList.add(agent);
        }

        return agentList;
    }

    public void removeAgent(Agent agent) throws IOException {
        String documentId = agent.getId();
        Document document = database.getDocument(documentId);
        database.deleteDocument(document);
    }

    public void removeAgent(String id) throws IOException {
        Document document = database.getDocument(id);
        database.deleteDocument(document);
    }

    @SuppressWarnings("unchecked")
    public void updateAgent(Agent agent) throws IOException {
        Document document = serializeService.toDocument(agent);
        database.saveDocument(document);
    }

}
