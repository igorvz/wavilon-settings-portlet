package com.aimprosoft.wavilon.service;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.PhoneNumber;

import java.io.IOException;
import java.util.List;

public interface GeneralService {

    <T> T  getModel(CouchModel model, Class<T> beanClass) throws IOException;

    CouchModel getModel(String id) throws IOException;

    List<CouchModel> getAvailableCouchModels(Long organizationId, CouchTypes type) throws IOException;
}
