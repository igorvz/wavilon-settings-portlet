package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.model.VirtualNumber;
import com.aimprosoft.wavilon.service.VirtualNumberDatabaseService;
import com.aimprosoft.wavilon.util.FormatUtil;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.ViewResults;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class VirtualNumberDBServiceImpl implements VirtualNumberDatabaseService {
    @Autowired
    private CouchDBService couchDBService;

    @Autowired
    private ObjectMapper objectMapper;


    private VirtualNumber getVirtualNumber(String id) throws IOException {
        CouchModel model = getModel(id);
        return objectMapper.convertValue(model.getProperties(), VirtualNumber.class);
    }

    public VirtualNumber getVirtualNumber(CouchModel model) throws IOException {
        return objectMapper.convertValue(model.getProperties(), VirtualNumber.class);
    }

    public CouchModel getModel(String id) throws IOException {
        return couchDBService.getModelById(id);
    }

    public List<VirtualNumber> getAllVirtualNumbers() throws IOException {
        ViewResults viewResults = couchDBService.database.adhoc(couchDBService.functions.getAllVirtualNumbersFunction());
        List<VirtualNumber> virtualNumberList = new LinkedList<VirtualNumber>();

        for (Document doc : viewResults.getResults()) {

            VirtualNumber virtualNumber = getVirtualNumber(doc.getId());

            virtualNumberList.add(virtualNumber);
        }
        return virtualNumberList;
    }

    public void updateVirtualNumber(VirtualNumber virtualNumber, CouchModel model) throws IOException {
        Map<String, Object> properties = objectMapper.convertValue(virtualNumber, Map.class);

        model.setProperties(properties);

        couchDBService.updateModel(model);
    }

    public List<CouchModel> getAllUsersCouchModelToVirtualNumber(Long userId, Long organizationId) throws IOException {
        String formattedFunction = FormatUtil.formatFunction(couchDBService.functions.getBaseModelsByUserAndTypeFunction(), "startnode", userId, organizationId);

        ViewResults viewResults = couchDBService.database.adhoc(formattedFunction);

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
