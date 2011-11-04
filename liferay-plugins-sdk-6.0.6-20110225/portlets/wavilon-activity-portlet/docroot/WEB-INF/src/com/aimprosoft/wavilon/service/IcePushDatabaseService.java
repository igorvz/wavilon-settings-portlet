package com.aimprosoft.wavilon.service;

import com.aimprosoft.wavilon.couch.PushModel;
import com.aimprosoft.wavilon.model.IcePushModel;

import java.io.IOException;
import java.util.List;

public interface IcePushDatabaseService {

    IcePushModel getIcePushModel(PushModel model) throws IOException;

    PushModel getModel(String id) throws IOException;

    List<IcePushModel> getAllIcePushModel() throws IOException;

    void updateIcePushModel(IcePushModel icePushModel, PushModel model) throws IOException;

    void addIcePushMode(IcePushModel icePushModel, PushModel model) throws IOException;

    void removeIcePushModel(PushModel model) throws IOException;

    void removeIcePushModel(String id) throws IOException;

}
