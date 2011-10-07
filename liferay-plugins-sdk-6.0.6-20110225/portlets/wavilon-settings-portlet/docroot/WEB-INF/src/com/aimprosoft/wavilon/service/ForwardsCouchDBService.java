package com.aimprosoft.wavilon.service;

import com.aimprosoft.wavilon.model.BaseModel;
import java.io.IOException;
import java.util.List;

public interface ForwardsCouchDBService {
    List<BaseModel> getAllForwards(Long userId, Long organizationId) throws IOException;
}
