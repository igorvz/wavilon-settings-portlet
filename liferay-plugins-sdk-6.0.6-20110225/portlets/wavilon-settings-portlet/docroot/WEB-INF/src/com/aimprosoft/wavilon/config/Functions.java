package com.aimprosoft.wavilon.config;

import org.springframework.beans.factory.annotation.Value;

public class Functions {

    @Value("${db.design.document.phonenumbers}")
    private String designDocumentPhoneNumbers;

    @Value("${db.design.document.nodes}")
    private String designDocumentNodes;

    @Value("${fetch.all.unique.entities}")
    private String allUniqueEntities;

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

    @Value("${fetch.phones.geo.number.id}")
    private String phonesGeoNumberId;

    @Value("${fetch.phones.virtual.number.id}")
    private String phonesVirtualNumberId;



    public String getDesignDocumentPhoneNumbers() {
        return designDocumentPhoneNumbers;
    }

    public String getDesignDocumentNodes() {
        return designDocumentNodes;
    }

    public String getAllUniqueEntities() {
        return allUniqueEntities;
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

    public String getPhonesGeoNumberId() {
        return phonesGeoNumberId;
    }

    public String getPhonesVirtualNumberId() {
        return phonesVirtualNumberId;
    }
}
