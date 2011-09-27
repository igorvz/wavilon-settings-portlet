package com.aimprosoft.wavilon.service;

import com.aimprosoft.wavilon.model.Extension;

import java.io.IOException;
import java.util.List;

public interface ExtensionDatabaseService {

    Extension getExtension(String id) throws IOException;

    List<Extension> getAllExtension() throws IOException;

    void updateExtension(Extension extension) throws IOException;

    List<Extension> getAllExtensionByUserId(Long userId, Long organizationId) throws IOException;

    void addExtension(Extension extension) throws IOException;

    void removeExtension(Extension extension) throws IOException;

    void removeExtension(String id) throws IOException;

}
