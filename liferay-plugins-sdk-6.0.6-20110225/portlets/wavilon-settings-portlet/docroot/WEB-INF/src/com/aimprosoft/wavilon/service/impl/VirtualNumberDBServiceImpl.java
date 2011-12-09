package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.model.VirtualNumber;
import com.aimprosoft.wavilon.service.VirtualNumberDatabaseService;
import org.codehaus.jackson.map.DeserializationConfig;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class VirtualNumberDBServiceImpl extends AbstractViewEntityService implements VirtualNumberDatabaseService {

    public VirtualNumber getVirtualNumber(CouchModel model) throws IOException {
        return objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false).convertValue(model.getProperties(), VirtualNumber.class);
    }


    public void updateVirtualNumber(VirtualNumber virtualNumber, CouchModel model) throws IOException {
        Map<String, Object> properties = objectMapper.convertValue(virtualNumber, Map.class);
        updateCouchModel(model, properties);
    }

    public void addVirtualNumber(VirtualNumber virtualNumber, CouchModel model) throws IOException {
        updateVirtualNumber(virtualNumber, model);
    }

}
