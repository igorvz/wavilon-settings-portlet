package com.aimprosoft.wavilon.config;

import org.springframework.beans.factory.annotation.Value;

public class Functions {

    @Value("${fetch.all.documents}")
    private String allDocumentFunction;

    public String getAllDocumentFunction() {
        return allDocumentFunction;
    }

    @Value("${fetch.all.agents}")
    private String allAgentFunction;

    public String getAllAgentFunction() {
        return allAgentFunction;
    }

    @Value("${fetch.all.queues}")
    private String allQueueFunction;

    public String getAllQueueFunction() {
        return allQueueFunction;
    }

    @Value("${fetch.all.extensions}")
    private String allExtensionFunction;

    public String getAllExtensionFunction() {
        return allExtensionFunction;
    }

    @Value("${fetch.all.recordings}")
    private String allRecordingFunction;

    public String getAllRecordingFunction() {
        return allRecordingFunction;
    }
}
