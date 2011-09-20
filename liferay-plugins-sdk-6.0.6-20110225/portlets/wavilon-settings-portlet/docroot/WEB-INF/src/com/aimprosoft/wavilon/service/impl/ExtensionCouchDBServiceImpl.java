package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.model.Extension;
import com.aimprosoft.wavilon.service.ExtensionDatabaseService;
import com.aimprosoft.wavilon.service.SerializeService;
import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.ViewResults;
import org.codehaus.jackson.map.ObjectReader;
import org.springframework.beans.factory.annotation.Required;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ExtensionCouchDBServiceImpl extends AbstractCouchDBService implements ExtensionDatabaseService {
    public Extension getExtension(String id) throws IOException {
        Document document = database.getDocument(id);
        return objectReader.readValue(document.toString());
    }

    public List<Extension> getAllExtension() throws IOException {

        ViewResults viewResults = database.getAllDocuments();
        List<Extension> extensionList = new LinkedList<Extension>();

        for (Document doc : viewResults.getResults()) {

            Extension extension = getExtension(doc.getId());

            extensionList.add(extension);
        }
        return extensionList;
    }

    @SuppressWarnings("unchecked")
    public void updateExtension(Extension extension) throws IOException {
        Document document = serializeService.toDocument(extension);
        database.saveDocument(document);
    }
}
