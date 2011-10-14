package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.config.Functions;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.PhoneModel;
import com.aimprosoft.wavilon.model.Agent;
import com.aimprosoft.wavilon.service.AllPhoneNumbersDatabaseService;
import com.aimprosoft.wavilon.service.SerializeService;
import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.ViewResults;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.codehaus.jackson.map.ObjectReader;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.beans.factory.annotation.Required;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AllPhoneNumbersDatabaseServiceImpl implements AllPhoneNumbersDatabaseService {

    private Database database;

    private ObjectReader objectReader;

    private ObjectWriter objectWriter;

    private Functions functions;

    @Required
    public void setDatabase(Database database) {
        this.database = database;
    }

    @Required
    public void setFunctions(Functions functions) {
        this.functions = functions;
    }

    @Required
    public void setObjectReader(ObjectReader objectReader) {
        this.objectReader = objectReader;
    }

    @Required
    public void setObjectWriter(ObjectWriter objectWriter) {
        this.objectWriter = objectWriter;
    }


    @SuppressWarnings("unchecked")
    public void updateModel(PhoneModel phoneModel) throws IOException {
        String jsonString = objectWriter.writeValueAsString(phoneModel);
        JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonString);
        Document document = new Document(json);

        database.saveDocument(document);
    }

    @SuppressWarnings("unchecked")
    public void updateModel(Long liferayOrganizationId, String id) throws IOException {
        PhoneModel phoneModel = getPhoneModel(id);
        phoneModel.setAllocationDate(System.nanoTime());
        phoneModel.setLiferayOrganizationId(liferayOrganizationId);

        updateModel(phoneModel);
    }

    public List<CouchModel> getVirtualNumbers() throws IOException {
        ViewResults viewResults = database.adhoc(functions.getAllPhonesVirtualNumbers());
        List<CouchModel> modelList = new LinkedList<CouchModel>();

        for (Document doc : viewResults.getResults()) {
            PhoneModel phoneModel = getPhoneModel(doc.getId());
            modelList.add(toVirtualNumber(phoneModel));
        }

        return modelList;
    }

    public List<CouchModel> getPhoneNumbers() throws IOException {
        ViewResults viewResults = database.adhoc(functions.getAllPhonesPhoneNumbers());
        List<CouchModel> modelList = new LinkedList<CouchModel>();

        for (Document doc : viewResults.getResults()) {
            PhoneModel phoneModel = getPhoneModel(doc.getId());
            modelList.add(toPhoneNumber(phoneModel));
        }

        return modelList;
    }

    public CouchModel getVirtualNumber(String documentId) throws IOException {
        return toVirtualNumber(getPhoneModel(documentId));
    }

    public CouchModel getPhoneNumber(String documentId) throws IOException {
        return toPhoneNumber(getPhoneModel(documentId));
    }

    private CouchModel toPhoneNumber(PhoneModel phoneModel) {
        CouchModel couchModel = createModel(phoneModel);

        Map<String, Object> outputs = new HashMap<String, Object>();
        outputs.put("startnode", "none");
        couchModel.setOutputs(outputs);

        return couchModel;
    }

    private CouchModel createModel(PhoneModel phoneModel) {
        CouchModel couchModel = new CouchModel();

        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("locator", phoneModel.getLocator());
        properties.put("name", "empty");


        couchModel.setId(phoneModel.getId());
        couchModel.setType(phoneModel.getType());
        couchModel.setRevision(phoneModel.getRevision());

        couchModel.setProperties(properties);
        return couchModel;
    }

    private CouchModel toVirtualNumber(PhoneModel phoneModel) {
        return createModel(phoneModel);
    }

    private PhoneModel getPhoneModel(String documentId) throws IOException {
        Document document = database.getDocument(documentId);

        return objectReader.readValue(document.toString());
    }

}
