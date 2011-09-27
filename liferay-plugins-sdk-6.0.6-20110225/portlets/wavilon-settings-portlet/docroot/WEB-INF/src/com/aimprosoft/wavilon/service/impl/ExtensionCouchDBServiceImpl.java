package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.model.Extension;
import com.aimprosoft.wavilon.service.ExtensionDatabaseService;
import com.aimprosoft.wavilon.util.FormatUtil;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.ViewResults;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ExtensionCouchDBServiceImpl extends AbstractCouchDBService implements ExtensionDatabaseService {

    public Extension getExtension(String id) throws IOException {
        return (Extension) getModelById(id);
    }

    public List<Extension> getAllExtension() throws IOException {

        ViewResults viewResults = database.adhoc(functions.getAllExtensionFunction());
        List<Extension> extensionList = new LinkedList<Extension>();

        for (Document doc : viewResults.getResults()) {

            Extension extension = getExtension(doc.getId());

            extensionList.add(extension);
        }
        return extensionList;
    }

    public void updateExtension(Extension extension) throws IOException {
        updateModel(extension);
    }

    public List<Extension> getAllExtensionByUserId(Long userId, Long organizationId) throws IOException {

        String formattedFunction = FormatUtil.formatFunction(functions.getBaseModelsByUserAndTypeFunction(), "extension", userId, organizationId);

        ViewResults viewResults = database.adhoc(formattedFunction);

        List<Extension> extensionList = new LinkedList<Extension>();

        for (Document doc : viewResults.getResults()) {

            Extension extension = getExtension(doc.getId());

            extensionList.add(extension);
        }
        return extensionList;
    }

    public void addExtension(Extension extension) throws IOException {
        updateExtension(extension);
    }

    public void removeExtension(Extension extension) throws IOException {
        removeModel(extension);
    }

    public void removeExtension(String id) throws IOException {
        removeModelById(id);
    }
}
