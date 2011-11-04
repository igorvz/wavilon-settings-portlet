package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.couch.PushModel;
import com.aimprosoft.wavilon.model.IcePushModel;
import com.aimprosoft.wavilon.service.IcePushDatabaseService;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.ViewResults;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class IcePushCouchDBServiceImpl extends AbstractPushViewEntityService implements IcePushDatabaseService {

    public IcePushModel getIcePushModel(PushModel model) throws IOException {
        return objectMapper.convertValue(model.getProperties(), IcePushModel.class);
    }

    public PushModel getModel(String id) throws IOException {
        return pushDBService.getModelById(id);
    }

    public List<IcePushModel> getAllIcePushModel() throws IOException {
        ViewResults viewResults = database.adhoc(functions.getPushentities());
        List<IcePushModel> numberList = new LinkedList<IcePushModel>();

        for (Document doc : viewResults.getResults()) {

            IcePushModel number = getIcePushModel(doc.getId());

            numberList.add(number);
        }
        return numberList;
    }

    public void updateIcePushModel(IcePushModel icePushModel, PushModel model) throws IOException {
        Map<String, Object> properties = objectMapper.convertValue(icePushModel, Map.class);

        model.setProperties(properties);

        pushDBService.updateModel(model);
    }

    public void addIcePushMode(IcePushModel icePushModel, PushModel model) throws IOException {
        updateIcePushModel(icePushModel, model);
    }

    public void removeIcePushModel(PushModel model) throws IOException {
        pushDBService.removeModel(model);
    }

    public void removeIcePushModel(String id) throws IOException {
        pushDBService.removeModelById(id);
    }

    private IcePushModel getIcePushModel(String id) throws IOException {
        PushModel model = getModel(id);
        return objectMapper.convertValue(model.getProperties(), IcePushModel.class);
    }
}
