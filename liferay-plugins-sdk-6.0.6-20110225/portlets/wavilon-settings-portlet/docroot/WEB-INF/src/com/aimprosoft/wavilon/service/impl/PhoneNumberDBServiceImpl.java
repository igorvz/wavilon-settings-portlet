package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.PhoneNumber;
import com.aimprosoft.wavilon.service.PhoneNumberDatabaseService;
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
public class PhoneNumberDBServiceImpl implements PhoneNumberDatabaseService {
    @Autowired
    private CouchDBService couchDBService;

    @Autowired
    private ObjectMapper objectMapper;


    private PhoneNumber getVirtualNumber(String id) throws IOException {
        CouchModel model = getModel(id);
        return objectMapper.convertValue(model.getProperties(), PhoneNumber.class);
    }


    public PhoneNumber getPhoneNumber(CouchModel model) throws IOException {
        return objectMapper.convertValue(model.getProperties(), PhoneNumber.class);
    }

    public CouchModel getModel(String id) throws IOException {
        return couchDBService.getModelById(id);
    }

    public List<PhoneNumber> getAllPhoneNumber() throws IOException {
        ViewResults viewResults = couchDBService.database.adhoc(couchDBService.functions.getAllPhoneNumbersFunction());
        List<PhoneNumber> numberList = new LinkedList<PhoneNumber>();

        for (Document doc : viewResults.getResults()) {

            PhoneNumber number = getVirtualNumber(doc.getId());

            numberList.add(number);
        }
        return numberList;
    }

    public void updatePhoneNumber(PhoneNumber number, CouchModel model, String forwardId) throws IOException {
        Map<String, Object> properties = objectMapper.convertValue(number, Map.class);
        Map<String, Object> outputs = new HashMap<String, Object>();
        outputs.put("startnode", forwardId);

        model.setProperties(properties);
        model.setOutputs(outputs);

        couchDBService.updateModel(model);
    }

    public List<CouchModel> getAllUsersCouchModelToPhoneNumber(Long userId, Long organizationId) throws IOException {
        String formattedFunction = FormatUtil.formatFunction(couchDBService.functions.getBaseModelsByUserAndTypeFunction(), CouchTypes.service, userId, organizationId);

        ViewResults viewResults = couchDBService.database.adhoc(formattedFunction);

        List<CouchModel> modelList = new LinkedList<CouchModel>();

        for (Document doc : viewResults.getResults()) {

            CouchModel model = getModel(doc.getId());

            modelList.add(model);
        }
        return modelList;
    }

    public void addPhoneNumber(PhoneNumber number, CouchModel model, String forwardId) throws IOException {
        updatePhoneNumber(number, model, forwardId);
    }

    public void removePhoneNumber(CouchModel model) throws IOException {
        couchDBService.removeModel(model);
    }

    public void removePhoneNumber(String id) throws IOException {
        couchDBService.removeModelById(id);
    }
}
