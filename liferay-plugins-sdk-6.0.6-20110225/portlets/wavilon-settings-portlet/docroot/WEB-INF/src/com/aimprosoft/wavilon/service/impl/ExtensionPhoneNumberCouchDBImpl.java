package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.model.ExtensionPhoneNumber;
import com.aimprosoft.wavilon.service.ExtensionPhoneNumberCouchDBService;
import com.aimprosoft.wavilon.util.FormatUtil;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.ViewResults;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ExtensionPhoneNumberCouchDBImpl extends AbstractCouchDBService implements ExtensionPhoneNumberCouchDBService {

    public void addExtensionPhoneNumber(ExtensionPhoneNumber phoneNumber) throws IOException {
        updateExtensionPhoneNumber(phoneNumber);
    }

    public ExtensionPhoneNumber getExtensionPhoneNumber(String id) throws IOException {
        return (ExtensionPhoneNumber) getModelById(id);
    }

    public List<ExtensionPhoneNumber> getAllExtensionPhoneNumber() throws IOException {
        ViewResults viewResults = database.adhoc(functions.getAllExtensionPhoneNumberFunction());

        List<ExtensionPhoneNumber> phoneNumbers = new LinkedList<ExtensionPhoneNumber>();

        for (Document doc : viewResults.getResults()) {
            ExtensionPhoneNumber phoneNumber = getExtensionPhoneNumber(doc.getId());

            phoneNumbers.add(phoneNumber);
        }

        return phoneNumbers;
    }

    public List<ExtensionPhoneNumber> getExtensionsPhoneNumberByUser(Long userId, Long organizationId) throws IOException {
        String formattedFunction = FormatUtil.formatFunction(functions.getBaseModelsByUserAndTypeFunction(), "extensionPhoneNumber", userId, organizationId);

        ViewResults viewResults = database.adhoc(formattedFunction);

        List<ExtensionPhoneNumber> phoneNumbers = new LinkedList<ExtensionPhoneNumber>();
        ExtensionPhoneNumber phoneNumber = null;

        for (Document doc : viewResults.getResults()) {
            phoneNumber = getExtensionPhoneNumber(doc.getId());
            phoneNumbers.add(phoneNumber);
        }

        return phoneNumbers;
    }

    public void removeExtensionPhoneNumber(ExtensionPhoneNumber extensionPhoneNumber) throws IOException {
        removeModel(extensionPhoneNumber);
    }

    public void removeExtensionPhoneNumber(String id) throws IOException {
        removeModelById(id);
    }

    public void updateExtensionPhoneNumber(ExtensionPhoneNumber phoneNumber) throws IOException {
        updateModel(phoneNumber);
    }
}
