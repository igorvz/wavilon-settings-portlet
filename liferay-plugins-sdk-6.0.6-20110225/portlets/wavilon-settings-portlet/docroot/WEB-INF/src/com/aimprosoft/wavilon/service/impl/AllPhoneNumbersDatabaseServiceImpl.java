package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.PhoneModel;
import com.aimprosoft.wavilon.service.AllPhoneNumbersDatabaseService;
import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.View;
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

public class AllPhoneNumbersDatabaseServiceImpl extends AbstractViewEntityService implements AllPhoneNumbersDatabaseService {

    private Database database;

    private ObjectReader objectReader;

    private ObjectWriter objectWriter;

    @Required
    public void setDatabase(Database database) {
        this.database = database;
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

    @Override
    public void updateModelsLiberationDate(String phoneModelId) throws IOException {
        PhoneModel phoneModel = getPhoneModel(phoneModelId);
        phoneModel.setLiberationDate(System.currentTimeMillis());

        updateModel(phoneModel);
    }

    @Override
    public void updateModelsAllocationDate(Long liferayOrganizationId, String phoneModelId) throws IOException {
        PhoneModel phoneModel = getPhoneModel(phoneModelId);
        phoneModel.setAllocationDate(System.currentTimeMillis());
        phoneModel.setLiberationDate(null);
        phoneModel.setLiferayOrganizationId(liferayOrganizationId);

        updateModel(phoneModel);
    }

    @SuppressWarnings("unchecked")
    public void updateModel(Long liferayOrganizationId, String phoneModelId) throws IOException {
        PhoneModel phoneModel = getPhoneModel(phoneModelId);
        phoneModel.setLiberationDate(System.nanoTime());
        phoneModel.setLiferayOrganizationId(liferayOrganizationId);

        updateModel(phoneModel);
    }

    public void updateModel(String phoneModelId) throws IOException {
        updateModel(null, phoneModelId);
    }

    public List<CouchModel> getVirtualNumbers() throws IOException {
        View view = database.getDocument(functions.getDesignDocumentPhoneNumbers()).getView(functions.getAllPhonesVirtualNumber());
        ViewResults viewResults = database.view(view);
        List<CouchModel> modelList = new LinkedList<CouchModel>();

        for (Document doc : viewResults.getResults()) {
            PhoneModel phoneModel = getPhoneModel(doc.getId());
            modelList.add(toVirtualNumber(phoneModel));
        }

        return modelList;
    }

    public List<CouchModel> getPhoneNumbers() throws IOException {
        View view = database.getDocument(functions.getDesignDocumentPhoneNumbers()).getView(functions.getAllPhonesPhoneNumber());
        ViewResults viewResults = database.view(view);
        List<CouchModel> modelList = new LinkedList<CouchModel>();

        for (Document doc : viewResults.getResults()) {
            PhoneModel phoneModel = getPhoneModel(doc.getId());
            modelList.add(toPhoneNumber(phoneModel));
        }

        return modelList;
    }

    public List<String> getOnlyPhoneNumbers(Long organizationId) throws IOException {
        View view = database.getDocument(functions.getDesignDocumentPhoneNumbers()).getView(functions.getAllPhonesPhoneNumberOutOfOrg());
        ViewResults viewResults = database.view(view);
        List<Document> totalNumsList = viewResults.getResults();

        view = database.getDocument(functions.getDesignDocumentPhoneNumbers()).getView(functions.getAllPhonesPhoneNumberNumsByOrg());
        view.setKey(String.valueOf(organizationId));
        viewResults = database.view(view);

        totalNumsList.removeAll(viewResults.getResults());
        totalNumsList.addAll(viewResults.getResults());

        List<String> modelList = new LinkedList<String>();

        for (Document doc : totalNumsList) {
            PhoneModel phoneModel = getPhoneModel(doc.getId());
            modelList.add(phoneModel.getLocator());
        }

        return modelList;
    }

    public List<String> getOnlyVirtualNumbers(Long organizationId) throws IOException {
        View view = database.getDocument(functions.getDesignDocumentPhoneNumbers()).getView(functions.getAllPhonesVirtualNumberOutOfOrg());
        ViewResults viewResults = database.view(view);
        List<Document> totalNumsList = viewResults.getResults();

        view = database.getDocument(functions.getDesignDocumentPhoneNumbers()).getView(functions.getAllPhonesVirtualNumberNumsByOrg());
        view.setKey(String.valueOf(organizationId));
        viewResults = database.view(view);

        totalNumsList.removeAll(viewResults.getResults());
        totalNumsList.addAll(viewResults.getResults());

        List<String> modelList = new LinkedList<String>();

        for (Document doc : totalNumsList) {
            PhoneModel phoneModel = getPhoneModel(doc.getId());
            modelList.add(phoneModel.getLocator());
        }
        return modelList;
    }

    public CouchModel getVirtualNumber(String documentId) throws IOException {
        return toVirtualNumber(getPhoneModel(documentId));
    }

    public CouchModel getPhoneNumber(String documentId) throws IOException {
        return toPhoneNumber(getPhoneModel(documentId));
    }

    public String getPhoneNumbersDocumentId(String locator) throws IOException {
        View view = database.getDocument(functions.getDesignDocumentPhoneNumbers()).getView(functions.getPhonesGeoNumberId());
        view.setKey(urlEncoder.encode("\"" + locator + "\""));
        ViewResults viewResults = database.view(view);

        Document document = viewResults.getResults().get(0);
        return document.getId();
    }


    public String getVirtualNumbersDocumentId(String locator) throws IOException {
        View view = database.getDocument(functions.getDesignDocumentPhoneNumbers()).getView(functions.getPhonesVirtualNumberId());
        view.setKey(urlEncoder.encode("\"" + locator + "\""));
        ViewResults viewResults = database.view(view);

        Document document = viewResults.getResults().get(0);
        return document.getId();
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
