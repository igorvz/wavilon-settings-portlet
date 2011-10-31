package com.aimprosoft.wavilon.config;

import org.springframework.beans.factory.annotation.Value;

public class Functions {

    @Value("${db.design.document.phonenumbers}")
    private String designDocumentPhonenumbers;

    @Value("${db.design.document.nodes}")
    private String designDocumentNodes;

    @Value("${fetch.all.unique.entities}")
    private String allUniqueEntitiess;

    @Value("${fetch.all.couch.model.lite}")
    private String allCouchModelLite;

    @Value("${couch.model.lite.name}")
    private String couchModelLiteName;

    @Value("${fetch.all.phones.virtual.number}")
    private String allPhonesVirtualNumber;

    @Value("${fetch.all.phones.phone.number}")
    private String allPhonesPhoneNumber;

    @Value("${fetch.extension.code.exist}")
    private String extensionCodeExist;

    public String getDesignDocumentPhonenumbers() {
        return designDocumentPhonenumbers;
    }

    public String getDesignDocumentNodes() {
        return designDocumentNodes;
    }

    public String getAllUniqueEntitiess() {
        return allUniqueEntitiess;
    }

    public String getAllCouchModelLite() {
        return allCouchModelLite;
    }

    public String getCouchModelLiteName() {
        return couchModelLiteName;
    }

    public String getAllPhonesVirtualNumber() {
        return allPhonesVirtualNumber;
    }

    public String getAllPhonesPhoneNumber() {
        return allPhonesPhoneNumber;
    }

    public String getExtensionCodeExist() {
        return extensionCodeExist;
    }
}
