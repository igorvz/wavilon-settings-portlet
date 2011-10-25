package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.service.AvatarService;
import com.aimprosoft.wavilon.service.SerializeService;
import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;
import net.sf.json.JSONObject;
import org.codehaus.jackson.map.ObjectReader;
import org.springframework.beans.factory.annotation.Required;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AvatarServiceImpl implements AvatarService {

    private Database database;

    @Required
    public void setDatabase(Database database) {
        this.database = database;
    }

}
