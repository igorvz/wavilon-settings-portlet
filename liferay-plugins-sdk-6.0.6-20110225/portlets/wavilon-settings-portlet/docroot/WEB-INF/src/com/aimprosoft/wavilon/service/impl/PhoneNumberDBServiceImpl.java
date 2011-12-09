package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.model.PhoneNumber;
import com.aimprosoft.wavilon.service.PhoneNumberDatabaseService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class PhoneNumberDBServiceImpl extends AbstractViewEntityService implements PhoneNumberDatabaseService {

    public void updatePhoneNumber(PhoneNumber number, CouchModel model, String forwardId) throws IOException {
        Map<String, Object> properties = objectMapper.convertValue(number, Map.class);
        Map<String, Object> outputs = new HashMap<String, Object>();
        outputs.put("startnode", forwardId);

        model.setOutputs(outputs);

        updateCouchModel(model, properties);
    }


    public void addPhoneNumber(PhoneNumber number, CouchModel model, String forwardId) throws IOException {
        updatePhoneNumber(number, model, forwardId);
    }

}
