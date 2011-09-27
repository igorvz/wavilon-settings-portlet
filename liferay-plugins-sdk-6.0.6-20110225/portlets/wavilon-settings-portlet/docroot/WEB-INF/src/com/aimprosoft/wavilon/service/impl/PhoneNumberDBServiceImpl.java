package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.model.PhoneNumber;
import com.aimprosoft.wavilon.service.PhoneNumberDatabaseService;
import com.aimprosoft.wavilon.util.FormatUtil;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.ViewResults;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class PhoneNumberDBServiceImpl extends AbstractCouchDBService implements PhoneNumberDatabaseService {

    public void addPhoneNumber(PhoneNumber phoneNumber) throws IOException {
        updatePhoneNumber(phoneNumber);
    }

    public PhoneNumber getPhoneNumber(String id) throws IOException {
        return (PhoneNumber) getModelById(id);
    }

    public List<PhoneNumber> getAllPhoneNumbers() throws IOException {
        ViewResults viewResults = database.adhoc(functions.getAllPhoneNumbersFunction());

        List<PhoneNumber> phoneNumberList = new LinkedList<PhoneNumber>();

        for (Document doc : viewResults.getResults()) {
            PhoneNumber phoneNumber = getPhoneNumber(doc.getId());

            phoneNumberList.add(phoneNumber);
        }

        return phoneNumberList;
    }

    public void removePhoneNumber(PhoneNumber phoneNumber) throws IOException {
        removeModel(phoneNumber);
    }

    public void removePhoneNumber(String id) throws IOException {
        removeModelById(id);
    }

    public void updatePhoneNumber(PhoneNumber phoneNumber) throws IOException {
        updateModel(phoneNumber);
    }

    public List<PhoneNumber> getAllPhoneNumbersByUser(Long userId, Long organizationId) throws IOException {
        String formattedFunction = FormatUtil.formatFunction(functions.getBaseModelsByUserAndTypeFunction(), "phoneNumber", userId, organizationId);

       ViewResults viewResults = database.adhoc(formattedFunction);

        List<PhoneNumber> phoneNumberList = new LinkedList<PhoneNumber>();

        for (Document doc : viewResults.getResults()) {
            PhoneNumber phoneNumber = getPhoneNumber(doc.getId());

            phoneNumberList.add(phoneNumber);
        }

        return phoneNumberList;
    }

}
