package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.model.Queue;
import com.aimprosoft.wavilon.service.QueueDatabaseService;
import org.codehaus.jackson.map.DeserializationConfig;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QueueCouchDBServiceImpl extends AbstractViewEntityService implements QueueDatabaseService {

    public Queue getQueue(CouchModel model) throws IOException {
        return objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false).convertValue(model.getProperties(), Queue.class);
    }

    public void updateQueue(Queue queue, CouchModel model, List<String> agents) throws IOException {
        Map<String, Object> properties = objectMapper.convertValue(queue, Map.class);
        Map<String, Object> outputs = new HashMap<String, Object>();
        outputs.put("agents", agents);

        model.setOutputs(outputs);

        updateCouchModel(model, properties);
    }

    public void addQueue(Queue queue, CouchModel model, List<String> agents) throws IOException {
        updateQueue(queue, model, agents);
    }

}
