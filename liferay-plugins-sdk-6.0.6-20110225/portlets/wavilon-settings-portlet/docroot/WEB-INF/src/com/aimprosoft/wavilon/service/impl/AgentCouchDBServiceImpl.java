package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.model.Agent;
import com.aimprosoft.wavilon.service.AgentDatabaseService;
import com.aimprosoft.wavilon.util.MappingUtil;
import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.ViewResults;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class AgentCouchDBServiceImpl implements AgentDatabaseService {

    private Database database;

    public void addAgent(Agent agent) throws IOException {
        Document document = MappingUtil.toDocument(agent);
        database.saveDocument(document);
    }


    public Agent getAgent(String id) throws IOException {
        Document document = database.getDocument(id);
        return MappingUtil.toAgent(document);
    }


    public List<Agent> getAllAgents() throws IOException {
        ViewResults viewResults = database.getAllDocuments();
        List<Agent> agentList = new LinkedList<Agent>();

        for (Document doc : viewResults.getResults()) {
            String documentId = doc.getId();

            Document document = database.getDocument(documentId);

            agentList.add(MappingUtil.toAgent(document));
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

    public void updateAgent(Agent agent) throws IOException {
        Document document = MappingUtil.toDocument(agent);
        database.saveDocument(document);
    }

    public void setDatabase(Database database) {
        this.database = database;
    }
}
