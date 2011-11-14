package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.service.AvatarService;
import org.ektorp.CouchDbConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class AvatarServiceImpl implements AvatarService {

    @Autowired
    @Qualifier("pushtestDatabaseConnector")
    private CouchDbConnector connector;

    public InputStream getAvatar(String attachmentId) {
        return connector.getAttachment("avatars", attachmentId);
    }

}
