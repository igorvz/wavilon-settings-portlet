package com.aimprosoft.wavilon.service;

import java.io.IOException;
import java.util.List;

public interface AllPhoneNumbersDatabaseService {

    void updateModelsLiberationDate(String phoneModelId) throws IOException;

    void updateModelsAllocationDate(Long liferayOrganizationId, String phoneModelId) throws IOException;

    List<String> getNumbers(Long organizationId, Object type) throws IOException;

    String getNumbersId(String locator, Object type) throws IOException;
}
