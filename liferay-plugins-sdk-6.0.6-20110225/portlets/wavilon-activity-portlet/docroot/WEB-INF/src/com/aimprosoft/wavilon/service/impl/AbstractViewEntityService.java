package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.config.Functions;
import org.apache.catalina.util.URLEncoder;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractViewEntityService {
    @Autowired
    protected Functions functions;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected URLEncoder urlEncoder;

}
