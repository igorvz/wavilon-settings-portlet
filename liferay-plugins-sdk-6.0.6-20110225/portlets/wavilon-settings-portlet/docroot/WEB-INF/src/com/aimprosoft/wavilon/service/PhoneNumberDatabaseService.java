package com.aimprosoft.wavilon.service;


import com.aimprosoft.wavilon.model.PhoneNumber;

import java.io.IOException;
import java.util.List;

public interface PhoneNumberDatabaseService {

    void addPhoneNumber(PhoneNumber phoneNumber) throws IOException;

    PhoneNumber getPhoneNumber(String id) throws IOException;

    List<PhoneNumber> getAllPhoneNumbers() throws IOException;

    List<PhoneNumber> getAllPhoneNumbersByUser(Long id, Long organizationId) throws IOException;

    void removePhoneNumber(PhoneNumber phoneNumber) throws IOException;

    void removePhoneNumber(String id) throws IOException;

    void updatePhoneNumber(PhoneNumber phoneNumber) throws IOException;


}
