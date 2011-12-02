package com.aimprosoft.wavilon.service;


import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.model.PhoneNumber;

import java.io.IOException;
import java.util.List;

public interface PhoneNumberDatabaseService extends GeneralService {

    PhoneNumber getPhoneNumber(CouchModel model) throws IOException;

    List<PhoneNumber> getAllPhoneNumber() throws IOException;

    void updatePhoneNumber(PhoneNumber number, CouchModel model, String id) throws IOException;

    List<CouchModel> getAllUsersCouchModelToPhoneNumber(Long organizationId) throws IOException;

    void addPhoneNumber(PhoneNumber number, CouchModel model, String forwardId) throws IOException;

    void removePhoneNumber(CouchModel model) throws IOException;

    void removePhoneNumber(String id) throws IOException;


}
