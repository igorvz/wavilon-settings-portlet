package com.aimprosoft.wavilon.service;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.PhoneModel;

import java.io.IOException;
import java.util.List;

public interface AllPhoneNumbersDatabaseService {

    public void updateModel(PhoneModel phoneModel) throws IOException;

    public void updateModel(Long liferayOrganizationId, String id) throws IOException;


    public List<CouchModel> getVirtualNumbers() throws IOException;

    public List<CouchModel> getPhoneNumbers() throws IOException;

    public CouchModel getVirtualNumber(String documentId) throws IOException;

    public CouchModel getPhoneNumber(String documentId) throws IOException;

}
