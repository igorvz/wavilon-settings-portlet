package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.Queue;
import com.aimprosoft.wavilon.service.QueueDatabaseService;
import com.aimprosoft.wavilon.util.FormatUtil;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.ViewResults;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class QueueCouchDBServiceImpl implements QueueDatabaseService {
    @Autowired
    private CouchDBService couchDBService;

    @Autowired
    private ObjectMapper objectMapper;

    private Queue getQueue(String id) throws IOException {
        CouchModel model = getModel(id);
        return objectMapper.convertValue(model.getProperties(), Queue.class);
    }

    public Queue getQueue(CouchModel model) throws IOException {
        return objectMapper.convertValue(model.getProperties(), Queue.class);
    }

    public CouchModel getModel(String id) throws IOException {
        return couchDBService.getModelById(id);
    }

    public List<Queue> getAllQueue() throws IOException {

        ViewResults viewResults = couchDBService.database.adhoc(couchDBService.functions.getAllQueueFunction());
        List<Queue> extensionList = new LinkedList<Queue>();

        for (Document doc : viewResults.getResults()) {

            Queue extension = getQueue(doc.getId());

            extensionList.add(extension);
        }
        return extensionList;
    }

    public void updateQueue(Queue queue, CouchModel model, List<String> agents) throws IOException {
        Map<String, Object> properties = objectMapper.convertValue(queue, Map.class);
        Map<String, Object> outputs = new HashMap<String, Object>();
        outputs.put("agents", agents);

        model.setProperties(properties);
        model.setOutputs(outputs);

        couchDBService.updateModel(model);
    }

    public List<CouchModel> getAllUsersCouchModelQueue(Long userId, Long organizationId) throws IOException {

        String formattedFunction = FormatUtil.formatFunction(couchDBService.functions.getBaseModelsByUserAndTypeFunction(), CouchTypes.queue, userId, organizationId);

        ViewResults viewResults = couchDBService.database.adhoc(formattedFunction);

        List<CouchModel> modelList = new LinkedList<CouchModel>();

        for (Document doc : viewResults.getResults()) {

            CouchModel model = getModel(doc.getId());

            modelList.add(model);
        }
        return modelList;
    }

    public void addQueue(Queue queue, CouchModel model, List<String> agents) throws IOException {
        updateQueue(queue, model, agents);
    }

    public void removeQueue(CouchModel model) throws IOException {
        couchDBService.removeModel(model);
    }

    public void removeQueue(String id) throws IOException {
        couchDBService.removeModelById(id);
    }

}
