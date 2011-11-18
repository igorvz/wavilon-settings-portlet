package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.model.PhoneNumber;
import com.aimprosoft.wavilon.service.PhoneNumberDatabaseService;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.View;
import com.fourspaces.couchdb.ViewResults;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class PhoneNumberDBServiceImpl extends AbstractViewEntityService implements PhoneNumberDatabaseService {

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
        ViewResults viewResults = database.adhoc(functions.getAllUniqueEntities());
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

        updateCouchModel(model);
    }

    public List<CouchModel> getAllUsersCouchModelToPhoneNumber(Long organizationId) throws IOException {
        View view = database.getDocument(functions.getDesignDocumentNodes()).getView(functions.getAllUniqueEntities());
        view.setKey(urlEncoder.encode("[\"service\"," + organizationId + "]"));
        ViewResults viewResults = database.view(view);

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
