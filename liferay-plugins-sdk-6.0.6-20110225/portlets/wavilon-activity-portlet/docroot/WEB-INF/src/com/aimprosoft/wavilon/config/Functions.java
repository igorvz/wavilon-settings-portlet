package com.aimprosoft.wavilon.config;

import org.springframework.beans.factory.annotation.Value;

public class Functions {

    @Value("${db.design.document.push.test}")
    private String pushTestDesignDocument;

    @Value("${db.design.document.notes.test}")
    private String notesTestDesignDocument;

    @Value("${db.function.push.test.all.models}")
    private String functionPushTestAllModels;

    @Value("${db.function.nodes.test.all.notes}")
    private String functionNodesTestAllNodes;

    @Value("${db.function.notes.test.filter.notes}")
    private String functionNodesTestFilterAllNodes;

    @Value("${db.function.push.test.lite.models}")
    private String functionPushTestLiteModels;

    public String getPushTestDesignDocument() {
        return pushTestDesignDocument;
    }

    public String getNotesTestDesignDocument() {
        return notesTestDesignDocument;
    }

    public String getFunctionPushTestAllModels() {
        return functionPushTestAllModels;
    }

    public String getFunctionNodesTestAllNodes() {
        return functionNodesTestAllNodes;
    }

    public String getFunctionNodesTestFilterAllNodes() {
        return functionNodesTestFilterAllNodes;
    }

    public String getFunctionPushTestLiteModels() {
        return functionPushTestLiteModels;
    }

    //    @Value("${db.design.document.push.test}")
//    private String pushTestDesignDocument;
//
//    @Value("${db.function.push.test.filter.notes}")
//    private String functionPushTestFilterNotes;
//
//    @Value("${db.function.push.test.all.notes}")
//    private String functionPushTestAllNotes;
//
//    public String getPushTestDesignDocument() {
//        return pushTestDesignDocument;
//    }
//
//    public String getFunctionPushTestFilterNotes() {
//        return functionPushTestFilterNotes;
//    }
//
//    public String getFunctionPushTestAllNotes() {
//        return functionPushTestAllNotes;
//    }
}
