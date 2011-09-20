package com.aimprosoft.wavilon.service;

import com.aimprosoft.wavilon.model.Extension;

import java.io.IOException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: orion
 * Date: 9/20/11
 * Time: 12:49 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ExtensionDatabaseService {

    Extension getExtension(String id) throws IOException;

    List<Extension> getAllExtension() throws IOException;

    void updateExtension(Extension extension) throws IOException;
}
