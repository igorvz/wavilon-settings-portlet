package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.config.Functions;
import com.aimprosoft.wavilon.model.Agent;
import com.aimprosoft.wavilon.service.SerializeService;
import com.fourspaces.couchdb.Database;
import org.codehaus.jackson.map.ObjectReader;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractCouchDBService {
    protected Database database;

    protected ObjectReader objectReader;

    protected Functions functions;

    protected SerializeService serializeService;

    @Required
    public void setDatabase(Database database) {
        this.database = database;
    }

    @Required
    public void setFunctions(Functions functions) {
        this.functions = functions;
    }

    @Required
    public void setObjectReader(ObjectReader objectReader) {
        this.objectReader = objectReader;
    }

    @Required
    public void setSerializeService(SerializeService<Agent> serializeService) {
        this.serializeService = serializeService;
    }

}
