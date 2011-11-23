package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.model.VirtualNumber;
import com.aimprosoft.wavilon.service.VirtualNumberDatabaseService;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.View;
import com.fourspaces.couchdb.ViewResults;
import org.codehaus.jackson.map.DeserializationConfig;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class VirtualNumberDBServiceImpl extends AbstractViewEntityService implements VirtualNumberDatabaseService {

    private VirtualNumber getVirtualNumber(String id) throws IOException {
        return getVirtualNumber(getModel(id));
    }

    public VirtualNumber getVirtualNumber(CouchModel model) throws IOException {
        return objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false).convertValue(model.getProperties(), VirtualNumber.class);
    }

    public CouchModel getModel(String id) throws IOException {
        return couchDBService.getModelById(id);
    }

    public List<VirtualNumber> getAllVirtualNumbers() throws IOException {
        ViewResults viewResults = database.adhoc(functions.getAllUniqueEntities());
        List<VirtualNumber> virtualNumberList = new LinkedList<VirtualNumber>();

        for (Document doc : viewResults.getResults()) {

            VirtualNumber virtualNumber = getVirtualNumber(doc.getId());

            virtualNumberList.add(virtualNumber);
        }
        return virtualNumberList;
    }

    public void updateVirtualNumber(VirtualNumber virtualNumber, CouchModel model) throws IOException {
        Map<String, Object> properties = objectMapper.convertValue(virtualNumber, Map.class);
        updateCouchModel(model, properties);
    }

    public List<CouchModel> getAllUsersCouchModelToVirtualNumber(Long organizationId) throws IOException {
        View view = database.getDocument(functions.getDesignDocumentNodes()).getView(functions.getAllUniqueEntities());
        view.setKey(urlEncoder.encode("[\"startnode\"," + organizationId + "]"));
        ViewResults viewResults = database.view(view);
        List<CouchModel> modelList = new LinkedList<CouchModel>();

        for (Document doc : viewResults.getResults()) {

            CouchModel model = getModel(doc.getId());

            modelList.add(model);
        }
        return modelList;
    }

    public void addVirtualNumber(VirtualNumber virtualNumber, CouchModel model) throws IOException {
        updateVirtualNumber(virtualNumber, model);
    }

    public void removeVirtualNumber(CouchModel model) throws IOException {
        couchDBService.removeModel(model);
    }

    public void removeVirtualNumber(String id) throws IOException {
        couchDBService.removeModelById(id);
    }
}
