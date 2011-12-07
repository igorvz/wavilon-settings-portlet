package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.couch.PhoneModel;
import com.aimprosoft.wavilon.service.AllPhoneNumbersDatabaseService;
import com.aimprosoft.wavilon.util.FormatUtil;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.springframework.beans.factory.annotation.Required;

import java.io.IOException;
import java.util.*;

public class AllPhoneNumbersDatabaseServiceImpl extends AbstractViewEntityService implements AllPhoneNumbersDatabaseService {

    private CouchDbConnector connector;

    @Required
    public void setConnector(CouchDbConnector connector) {
        this.connector = connector;
    }

    @SuppressWarnings("unchecked")
    private void updateModel(PhoneModel phoneModel) throws IOException {
        connector.update(phoneModel);
    }

    public void updateModelsLiberationDate(String phoneModelId) throws IOException {
        PhoneModel phoneModel = getPhoneModel(phoneModelId);
        phoneModel.setLiberationDate(System.currentTimeMillis());

        updateModel(phoneModel);
    }

    public void updateModelsAllocationDate(Long liferayOrganizationId, String phoneModelId) throws IOException {
        PhoneModel phoneModel = getPhoneModel(phoneModelId);
        phoneModel.setAllocationDate(System.currentTimeMillis());
        phoneModel.setLiberationDate(null);
        phoneModel.setLiferayOrganizationId(liferayOrganizationId);

        updateModel(phoneModel);
    }

    private PhoneModel getPhoneModel(String documentId) throws IOException {
        return connector.get(PhoneModel.class, documentId);
    }

    public String getNumbersId(String locator, Object type) throws IOException {
        String viewName;

        if (FormatUtil.isSameType(type, CouchTypes.startnode)) {
            viewName = functions.getPhonesVirtualNumberId();
        } else {
            viewName = functions.getPhonesGeoNumberId();
        }

        ViewQuery query = new ViewQuery()
                .designDocId(functions.getDesignDocumentPhoneNumbers())
                .viewName(viewName)
                .key(locator);

        return (String) connector.queryView(query, HashMap.class).get(0).get("id");
    }

    public List<String> getNumbers(Long organizationId, Object type) throws IOException {
        String allNullView;
        String currentOrgView;

        if (FormatUtil.isSameType(type, CouchTypes.startnode)) {
            allNullView = functions.getPhonesViewAllVirtual();
            currentOrgView = functions.getPhonesViewOrgVirtual();
        } else {
            allNullView = functions.getPhonesViewAllGeo();
            currentOrgView = functions.getPhonesViewOrgGeo();
        }

        ViewQuery query = new ViewQuery()
                .designDocId(functions.getDesignDocumentPhoneNumbersSettings())
                .queryParam(functions.getPhonesAvailableDateVarName(), String.valueOf(System.currentTimeMillis()))
                .listName(functions.getPhonesList()).viewName(allNullView);
        List<PhoneModel> availablePhoneModel = connector.queryView(query, PhoneModel.class);

        query = new ViewQuery().designDocId(functions.getDesignDocumentPhoneNumbersSettings())
                .viewName(currentOrgView)
                .key(organizationId);
        List<PhoneModel> availableByOrganisationPhoneModel = connector.queryView(query, PhoneModel.class);

        Set<PhoneModel> totalSet = new HashSet<PhoneModel>(availablePhoneModel);
        totalSet.addAll(availableByOrganisationPhoneModel);


        List<String> modelList = new LinkedList<String>();

        for (PhoneModel phoneModel : totalSet) {
            modelList.add(phoneModel.getLocator());
        }

        return modelList;
    }


}
