package com.aimprosoft.wavilon.service;

import com.aimprosoft.wavilon.model.ExtensionGtalk;

import java.io.IOException;
import java.util.List;

public interface ExtensionGtalkCouchDBService {

    void addExtensionGtalk(ExtensionGtalk gtalk) throws IOException;

    ExtensionGtalk getExtensionGtalk(String id) throws IOException;

    List<ExtensionGtalk> getAllExtensionGtalk() throws IOException;

    List<ExtensionGtalk> getExtensionGtalkByUser(Long id, Long organizationId) throws IOException;

    void removeExtensionGtalk(ExtensionGtalk gtalk) throws IOException;

    void removeExtensionGtalk(String id) throws IOException;

    void updateExtensionGtalk(ExtensionGtalk gtalk) throws IOException;
}

