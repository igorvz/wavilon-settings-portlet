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

    @Value("${db.design.document.phonenumbers.settings}")
    private String designDocumentPhoneNumbersSettings;

    @Value("${fetch.phones.view.all.virtual}")
    private String phonesViewAllVirtual;

    @Value("${fetch.phones.view.all.geo}")
    private String phonesViewAllGeo;

    @Value("${fetch.phones.view.by.org.virtual}")
    private String phonesViewOrgVirtual;

    @Value("${fetch.phones.view.by.org.geo}")
    private String phonesViewOrgGeo;

    @Value("${fetch.phones.available.date.var.name}")
    private String phonesAvailableDateVarName;

    @Value("${fetch.phones.list.available.phones}")
    private String phonesList;

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

    public String getDesignDocumentPhoneNumbersSettings() {
        return designDocumentPhoneNumbersSettings;
    }

    public String getPhonesViewAllVirtual() {
        return phonesViewAllVirtual;
    }

    public String getPhonesViewAllGeo() {
        return phonesViewAllGeo;
    }

    public String getPhonesViewOrgVirtual() {
        return phonesViewOrgVirtual;
    }

    public String getPhonesViewOrgGeo() {
        return phonesViewOrgGeo;
    }

    public String getPhonesAvailableDateVarName() {
        return phonesAvailableDateVarName;
    }

    public String getPhonesList() {
        return phonesList;
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
