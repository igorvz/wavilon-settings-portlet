package com.aimprosoft.wavilon.service;


import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.model.PhoneNumber;

import java.io.IOException;

public interface PhoneNumberDatabaseService extends GeneralService {

    void updatePhoneNumber(PhoneNumber number, CouchModel model, String id) throws IOException;

    void addPhoneNumber(PhoneNumber number, CouchModel model, String forwardId) throws IOException;

}
