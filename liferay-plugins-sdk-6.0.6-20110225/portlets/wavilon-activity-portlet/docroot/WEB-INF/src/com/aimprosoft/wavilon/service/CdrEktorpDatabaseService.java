package com.aimprosoft.wavilon.service;

import com.aimprosoft.wavilon.model.CdrModel;

import java.io.IOException;
import java.util.List;

public interface CdrEktorpDatabaseService {

    CdrModel getModel(String id) throws IOException;

    List<CdrModel> getAllCdrModels() throws IOException;

    void addCdrModel(CdrModel model) throws IOException;

    void removeCdrModel(CdrModel model) throws IOException;

    void removeCdrModel(String id) throws IOException;

    public List<String> getModelsId();
}
