package com.aimprosoft.wavilon.service;

import com.aimprosoft.wavilon.model.ExtensionSipUrl;

import java.io.IOException;
import java.util.List;

public interface ExtensionSIPUrlCouchDBService {

    void addExtensionSipUrl(ExtensionSipUrl url) throws IOException;

    ExtensionSipUrl getExtensionSipUrl(String id) throws IOException;

    List<ExtensionSipUrl> getAllExtensionSipUrl() throws IOException;

    List<ExtensionSipUrl> getExtensionSipUrlByUser(Long id, Long organizationId) throws IOException;

    void removeExtensionSipUrl(ExtensionSipUrl url) throws IOException;

    void removeExtensionSipUrl(String id) throws IOException;

    void updateExtensionSipUrl(ExtensionSipUrl url) throws IOException;
}
