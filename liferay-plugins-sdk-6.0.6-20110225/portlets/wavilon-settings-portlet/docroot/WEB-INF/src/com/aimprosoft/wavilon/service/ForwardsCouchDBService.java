package com.aimprosoft.wavilon.service;

import com.aimprosoft.wavilon.couch.CouchModelLite;

import java.io.IOException;
import java.util.List;

public interface ForwardsCouchDBService {
    List<CouchModelLite> getAllForwards(Long userId, Long organizationId) throws IOException;
}
