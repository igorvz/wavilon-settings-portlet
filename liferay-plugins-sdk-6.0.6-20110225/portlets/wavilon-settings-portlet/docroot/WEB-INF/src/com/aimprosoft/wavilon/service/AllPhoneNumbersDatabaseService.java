package com.aimprosoft.wavilon.service;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.PhoneModel;

import java.io.IOException;
import java.util.List;

public interface AllPhoneNumbersDatabaseService {

    void updateModel(PhoneModel phoneModel) throws IOException;

    void updateModel(String PhoneModelId) throws IOException;

    void updateModel(Long liferayOrganizationId, String PhoneModelId) throws IOException;

    List<CouchModel> getVirtualNumbers() throws IOException;

    List<CouchModel> getPhoneNumbers() throws IOException;

    List<String> getOnlyPhoneNumbers() throws IOException;

    CouchModel getVirtualNumber(String documentId) throws IOException;

    CouchModel getPhoneNumber(String documentId) throws IOException;

    public String getDocumentId(String locator) throws IOException;
}
