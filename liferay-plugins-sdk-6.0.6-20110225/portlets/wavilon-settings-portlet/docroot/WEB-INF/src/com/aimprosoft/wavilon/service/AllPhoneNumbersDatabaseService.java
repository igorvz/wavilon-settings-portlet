package com.aimprosoft.wavilon.service;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.PhoneModel;

import java.io.IOException;
import java.util.List;

public interface AllPhoneNumbersDatabaseService {

    void updateModel(PhoneModel phoneModel) throws IOException;

    void updateModel(String PhoneModelId) throws IOException;

    void updateModelsLiberationDate(String phoneModelId) throws IOException;

    void updateModelsAllocationDate(Long liferayOrganizationId, String phoneModelId) throws IOException;

    void updateModel(Long liferayOrganizationId, String PhoneModelId) throws IOException;

    List<CouchModel> getVirtualNumbers() throws IOException;

    List<CouchModel> getPhoneNumbers() throws IOException;

    List<String> getOnlyPhoneNumbers(Long organizationId) throws IOException;

    List<String> getOnlyVirtualNumbers(Long organizationId) throws IOException;

    CouchModel getVirtualNumber(String documentId) throws IOException;

    CouchModel getPhoneNumber(String documentId) throws IOException;

    String getPhoneNumbersDocumentId(String locator) throws IOException;

    String getVirtualNumbersDocumentId(String locator) throws IOException;
}
