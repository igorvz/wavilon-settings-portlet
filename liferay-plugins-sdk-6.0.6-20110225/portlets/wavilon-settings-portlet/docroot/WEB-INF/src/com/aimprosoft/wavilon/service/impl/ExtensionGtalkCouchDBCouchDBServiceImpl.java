package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.model.ExtensionGtalk;
import com.aimprosoft.wavilon.service.ExtensionGtalkCouchDBService;
import com.aimprosoft.wavilon.util.FormatUtil;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.ViewResults;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ExtensionGtalkCouchDBCouchDBServiceImpl extends AbstractCouchDBService implements ExtensionGtalkCouchDBService {

    public void addExtensionGtalk(ExtensionGtalk gtalk) throws IOException {
        updateExtensionGtalk(gtalk);
    }

    public ExtensionGtalk getExtensionGtalk(String id) throws IOException {
        return (ExtensionGtalk) getModelById(id);
    }

    public List<ExtensionGtalk> getAllExtensionGtalk() throws IOException {
        ViewResults viewResults = database.adhoc(functions.getAllExtensionGtalkFunction());

        List<ExtensionGtalk> gtalks = new LinkedList<ExtensionGtalk>();

        for (Document doc : viewResults.getResults()) {
            ExtensionGtalk agent = getExtensionGtalk(doc.getId());

            gtalks.add(agent);
        }

        return gtalks;
    }

    public List<ExtensionGtalk> getExtensionGtalkByUser(Long userId, Long organizationId) throws IOException {
        String formattedFunction = FormatUtil.formatFunction(functions.getBaseModelsByUserAndTypeFunction(), "extensionGtalk", userId, organizationId);

        ViewResults viewResults = database.adhoc(formattedFunction);

        List<ExtensionGtalk> gtalks = new LinkedList<ExtensionGtalk>();

        for (Document doc : viewResults.getResults()) {
            ExtensionGtalk gtalk = getExtensionGtalk(doc.getId());

            gtalks.add(gtalk);
        }

        return gtalks;
}

    public void removeExtensionGtalk(ExtensionGtalk gtalk) throws IOException {
        removeModel(gtalk);
    }

    public void removeExtensionGtalk(String id) throws IOException {
        removeModelById(id);
    }

    public void updateExtensionGtalk(ExtensionGtalk gtalk) throws IOException {
        updateModel(gtalk);
    }
}
