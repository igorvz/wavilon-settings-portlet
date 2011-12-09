package com.aimprosoft.wavilon.service;


import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.model.VirtualNumber;

import java.io.IOException;

public interface VirtualNumberDatabaseService  extends GeneralService {

    VirtualNumber getVirtualNumber(CouchModel model) throws IOException;

    void updateVirtualNumber(VirtualNumber virtualNumber, CouchModel model) throws IOException;

    void addVirtualNumber(VirtualNumber virtualNumber, CouchModel model) throws IOException;
}
