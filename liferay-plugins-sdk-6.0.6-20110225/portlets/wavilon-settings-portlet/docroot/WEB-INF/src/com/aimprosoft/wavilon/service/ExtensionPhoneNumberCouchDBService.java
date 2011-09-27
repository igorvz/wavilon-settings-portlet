package com.aimprosoft.wavilon.service;

import com.aimprosoft.wavilon.model.ExtensionPhoneNumber;

import java.io.IOException;
import java.util.List;

public interface ExtensionPhoneNumberCouchDBService {

    void addExtensionPhoneNumber(ExtensionPhoneNumber phoneNumber) throws IOException;

    ExtensionPhoneNumber getExtensionPhoneNumber(String id) throws IOException;

    List<ExtensionPhoneNumber> getAllExtensionPhoneNumber() throws IOException;

    List<ExtensionPhoneNumber> getExtensionsPhoneNumberByUser(Long id, Long organizationId) throws IOException;

    void removeExtensionPhoneNumber(ExtensionPhoneNumber extensionPhoneNumber) throws IOException;

    void removeExtensionPhoneNumber(String id) throws IOException;

    void updateExtensionPhoneNumber(ExtensionPhoneNumber phoneNumber) throws IOException;

}
