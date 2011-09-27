package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.model.ExtensionSipUrl;
import com.aimprosoft.wavilon.service.ExtensionSIPUrlCouchDBService;
import com.aimprosoft.wavilon.util.FormatUtil;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.ViewResults;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ExtensionSipURLCouchDBService extends AbstractCouchDBService implements ExtensionSIPUrlCouchDBService {

    public void addExtensionSipUrl(ExtensionSipUrl url) throws IOException {
        updateExtensionSipUrl(url);
    }

    public ExtensionSipUrl getExtensionSipUrl(String id) throws IOException {
        return (ExtensionSipUrl) getModelById(id);
    }

    public List<ExtensionSipUrl> getAllExtensionSipUrl() throws IOException {
        ViewResults viewResults = database.adhoc(functions.getAllExtensionSipURLFunction());

        List<ExtensionSipUrl> sipUrls = new LinkedList<ExtensionSipUrl>();

        for (Document doc : viewResults.getResults()) {
            ExtensionSipUrl sipUrl = getExtensionSipUrl(doc.getId());

            sipUrls.add(sipUrl);
        }

        return sipUrls;
    }

    public List<ExtensionSipUrl> getExtensionSipUrlByUser(Long userId, Long organizationId) throws IOException {
        String formattedFunction = FormatUtil.formatFunction(functions.getBaseModelsByUserAndTypeFunction(), "extensionSipUrl", userId, organizationId);

        ViewResults viewResults = database.adhoc(formattedFunction);

        List<ExtensionSipUrl> sipUrls = new LinkedList<ExtensionSipUrl>();

        for (Document doc : viewResults.getResults()) {
            ExtensionSipUrl sipUrl = getExtensionSipUrl(doc.getId());

            sipUrls.add(sipUrl);
        }
        return sipUrls;
    }

    public void removeExtensionSipUrl(ExtensionSipUrl url) throws IOException {
        removeModel(url);
    }

    public void removeExtensionSipUrl(String id) throws IOException {
        removeModelById(id);
    }

    public void updateExtensionSipUrl(ExtensionSipUrl url) throws IOException {
        updateModel(url);
    }
}
