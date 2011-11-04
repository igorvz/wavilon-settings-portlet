package com.aimprosoft.wavilon.config;

import org.springframework.beans.factory.annotation.Value;

public class Functions {

    @Value("${db.design.document.push}")
    private String designDocumentPhonenumbers;

    @Value("${fetch.all.unique.push}")
    private String pushentities;

    public String getDesignDocumentPhonenumbers() {
        return designDocumentPhonenumbers;
    }

    public String getPushentities() {
        return pushentities;
    }
}
