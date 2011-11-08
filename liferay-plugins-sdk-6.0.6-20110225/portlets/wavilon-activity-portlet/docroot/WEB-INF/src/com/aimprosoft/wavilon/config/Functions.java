package com.aimprosoft.wavilon.config;

import org.springframework.beans.factory.annotation.Value;

public class Functions {

    @Value("${db.design.document.push.test}")
    private String pushTestDesignDocument;

    @Value("${db.function.push.test.filter.notes}")
    private String functionPushTestFilterNotes;

    @Value("${db.function.push.test.all.notes}")
    private String functionPushTestAllNotes;

    public String getPushTestDesignDocument() {
        return pushTestDesignDocument;
    }

    public String getFunctionPushTestFilterNotes() {
        return functionPushTestFilterNotes;
    }

    public String getFunctionPushTestAllNotes() {
        return functionPushTestAllNotes;
    }
}
