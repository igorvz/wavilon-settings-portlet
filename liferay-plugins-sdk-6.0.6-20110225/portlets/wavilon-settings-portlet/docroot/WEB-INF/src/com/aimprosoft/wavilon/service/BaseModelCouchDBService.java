package com.aimprosoft.wavilon.service;


import com.aimprosoft.wavilon.model.BaseModel;

import java.io.IOException;

public interface BaseModelCouchDBService {
    BaseModel getBaseModel(String id) throws IOException;
}
