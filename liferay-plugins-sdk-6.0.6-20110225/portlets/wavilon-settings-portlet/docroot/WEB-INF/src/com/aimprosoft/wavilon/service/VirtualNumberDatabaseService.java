package com.aimprosoft.wavilon.service;


import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.model.VirtualNumber;

import java.io.IOException;
import java.util.List;

public interface VirtualNumberDatabaseService  extends GeneralService {

    VirtualNumber getVirtualNumber(CouchModel model) throws IOException;

    List<VirtualNumber> getAllVirtualNumbers() throws IOException;

    void updateVirtualNumber(VirtualNumber virtualNumber, CouchModel model) throws IOException;

    List<CouchModel> getAllUsersCouchModelToVirtualNumber(Long organizationId) throws IOException;

    void addVirtualNumber(VirtualNumber virtualNumber, CouchModel model) throws IOException;

    void removeVirtualNumber(CouchModel model) throws IOException;

    void removeVirtualNumber(String id) throws IOException;
}
