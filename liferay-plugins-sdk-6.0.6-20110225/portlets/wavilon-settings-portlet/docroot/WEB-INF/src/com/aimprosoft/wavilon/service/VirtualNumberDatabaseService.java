package com.aimprosoft.wavilon.service;


import com.aimprosoft.wavilon.model.VirtualNumber;

import java.io.IOException;
import java.util.List;

public interface VirtualNumberDatabaseService {

    void addVirtualNumber(VirtualNumber virtualNumber) throws IOException;

    VirtualNumber getVirtualNumber(String id) throws IOException;

    List<VirtualNumber> getAllVirtualNumbers() throws IOException;

    List<VirtualNumber> getAllVirtualNumbersByUser(Long id, Long organizationId) throws IOException;

    void removeVirtualNumber(VirtualNumber virtualNumber) throws IOException;

    void removeVirtualNumber(String id) throws IOException;

    void updateVirtualNumber(VirtualNumber virtualNumber) throws IOException;


}
