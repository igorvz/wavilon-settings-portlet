package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.model.VirtualNumber;
import com.aimprosoft.wavilon.service.VirtualNumberDatabaseService;
import com.aimprosoft.wavilon.util.FormatUtil;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.ViewResults;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class VirtualNumberDBServiceImpl extends AbstractCouchDBService implements VirtualNumberDatabaseService {

    public void addVirtualNumber(VirtualNumber virtualNumber) throws IOException {
        updateVirtualNumber(virtualNumber);
    }

    public VirtualNumber getVirtualNumber(String id) throws IOException {
        return (VirtualNumber) getModelById(id);
    }

    public List<VirtualNumber> getAllVirtualNumbers() throws IOException {
        ViewResults viewResults = database.adhoc(functions.getAllVirtualNumbersFunction());

        List<VirtualNumber> virtualNumberList = new LinkedList<VirtualNumber>();

        for (Document doc : viewResults.getResults()) {
            VirtualNumber virtualNumber = getVirtualNumber(doc.getId());

            virtualNumberList.add(virtualNumber);
        }

        return virtualNumberList;
    }

    public void removeVirtualNumber(VirtualNumber virtualNumber) throws IOException {
        removeModel(virtualNumber);
    }

    public void removeVirtualNumber(String id) throws IOException {
        removeModelById(id);
    }

    public void updateVirtualNumber(VirtualNumber virtualNumber) throws IOException {
        updateModel(virtualNumber);
    }

    public List<VirtualNumber> getAllVirtualNumbersByUser(Long userId, Long organizationId) throws IOException {
        String formattedFunction = FormatUtil.formatFunction(functions.getBaseModelsByUserAndTypeFunction(), "virtualNumber", userId, organizationId);

       ViewResults viewResults = database.adhoc(formattedFunction);

        List<VirtualNumber> virtualNumberList = new LinkedList<VirtualNumber>();

        for (Document doc : viewResults.getResults()) {
            VirtualNumber virtualNumber = getVirtualNumber(doc.getId());

            virtualNumberList.add(virtualNumber);
        }

        return virtualNumberList;
    }

}
