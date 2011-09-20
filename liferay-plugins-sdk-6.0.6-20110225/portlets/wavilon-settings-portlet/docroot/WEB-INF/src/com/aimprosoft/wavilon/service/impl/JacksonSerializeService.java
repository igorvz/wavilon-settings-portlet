package com.aimprosoft.wavilon.service.impl;

import com.aimprosoft.wavilon.service.SerializeService;
import com.fourspaces.couchdb.Document;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.beans.factory.annotation.Required;

import java.io.IOException;

public class JacksonSerializeService<T> implements SerializeService<T>{

    private ObjectWriter objectWriter;

    @Required
    public void setObjectWriter(ObjectWriter objectWriter) {
        this.objectWriter = objectWriter;
    }

    public Document toDocument(T object) throws IOException {
        String jsonString = objectWriter.writeValueAsString(object);
        JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonString);

        discardIfNull(json);

        return new Document(json);
    }

    private void discardIfNull(JSONObject json) {
        //discard revision if it is null
        discardIfNull(json, "_rev");
    }

    private void discardIfNull(JSONObject json, String field) {
        if (json.getString(field) == null || "null".equalsIgnoreCase(json.getString(field))){
            json.discard(field);
        }
    }
}
