package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.service.ForwardsCouchDBService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ForwardsCouchDBServiceImpl implements ForwardsCouchDBService {
    public List<CouchModelLite> getAllForwards(Long userId, Long organizationId) throws IOException {

        return null;
    }
}
