package com.aimprosoft.wavilon.service;

import com.fourspaces.couchdb.Document;

import java.io.IOException;

public interface SerializeService<T> {

    Document toDocument(T object) throws IOException;

}
