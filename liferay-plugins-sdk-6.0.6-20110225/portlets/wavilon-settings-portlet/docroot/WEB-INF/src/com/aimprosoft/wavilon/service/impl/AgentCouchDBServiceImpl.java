package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.model.Agent;
import com.aimprosoft.wavilon.service.AgentDatabaseService;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.View;
import com.fourspaces.couchdb.ViewResults;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class AgentCouchDBServiceImpl extends AbstractViewEntityService implements AgentDatabaseService {

    public Agent getAgent(String id) throws IOException {
        return getAgent(getModel(id));
    }

    public Agent getAgent(CouchModel model) throws IOException {
        return objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false).convertValue(model.getProperties(), Agent.class);
    }

    public CouchModel getModel(String id) throws IOException {
        return couchDBService.getModelById(id);
    }

    public List<Agent> getAllAgent() throws IOException {

        ViewResults viewResults = database.adhoc(functions.getAllUniqueEntities());
        List<Agent> extensionList = new LinkedList<Agent>();

        for (Document doc : viewResults.getResults()) {

            Agent extension = getAgent(doc.getId());

            extensionList.add(extension);
        }
        return extensionList;
    }

    public void updateAgent(Agent agent, CouchModel model, String extension) throws IOException {
        Map<String, Object> properties = objectMapper.convertValue(agent, Map.class);
        Map<String, Object> outputs = new HashMap<String, Object>();
        outputs.put("extension", extension);

        model.setOutputs(outputs);

        updateCouchModel(model, properties);

    }

    public List<CouchModel> getAllUsersCouchModelAgent(Long organizationId) throws IOException {

        View view = database.getDocument(functions.getDesignDocumentNodes()).getView(functions.getAllUniqueEntities());
        view.setKey(urlEncoder.encode("[\"agent\"," + organizationId + "]"));
        ViewResults viewResults = database.view(view);

        List<CouchModel> modelList = new LinkedList<CouchModel>();

        for (Document doc : viewResults.getResults()) {

            CouchModel model = getModel(doc.getId());

            modelList.add(model);
        }
        return modelList;
    }

    public void addAgent(Agent agent, CouchModel model, String extension) throws IOException {
        updateAgent(agent, model, extension);
    }

    public void removeAgent(CouchModel model) throws IOException {
        couchDBService.removeModel(model);
    }

    public void removeAgent(String id) throws IOException {
        couchDBService.removeModelById(id);
    }
}
