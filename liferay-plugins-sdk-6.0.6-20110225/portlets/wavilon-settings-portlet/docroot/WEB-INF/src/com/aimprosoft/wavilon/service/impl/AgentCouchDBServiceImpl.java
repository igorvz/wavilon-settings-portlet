package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.Agent;
import com.aimprosoft.wavilon.service.AgentDatabaseService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AgentCouchDBServiceImpl extends AbstractViewEntityService implements AgentDatabaseService {

    public void updateAgent(Agent agent, CouchModel model, String extension) throws IOException {
        Map<String, Object> properties = objectMapper.convertValue(agent, Map.class);
        Map<String, Object> outputs = new HashMap<String, Object>();
        outputs.put("extension", extension);

        model.setOutputs(outputs);

        updateCouchModel(model, properties);

    }

    public List<CouchModel> getAllUsersCouchModelAgent(Long organizationId) throws IOException {
        return getAvailableCouchModels(organizationId, CouchTypes.agent);
    }

    public void addAgent(Agent agent, CouchModel model, String extension) throws IOException {
        updateAgent(agent, model, extension);
    }

}
