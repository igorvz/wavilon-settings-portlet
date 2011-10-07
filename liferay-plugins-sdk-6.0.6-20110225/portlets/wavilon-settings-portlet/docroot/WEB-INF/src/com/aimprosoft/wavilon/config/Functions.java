package com.aimprosoft.wavilon.config;

import org.springframework.beans.factory.annotation.Value;

public class Functions {

    @Value("${fetch.all.documents}")
    private String allDocumentFunction;

    @Value("${fetch.all.agents}")
    private String allAgentFunction;

    @Value("${fetch.all.base.model.by.user.and.type}")
    private String baseModelsByUserAndTypeFunction;

    @Value("${fetch.all.queues}")
    private String allQueueFunction;

    @Value("${fetch.all.extensions}")
    private String allExtensionFunction;

    @Value("${fetch.all.recordings}")
    private String allRecordingFunction;

    @Value("${fetch.all.phone.numbers}")
    private String allPhoneNumbers;

    @Value("${fetch.all.extensions.gtalk}")
    private String allExtensionGtalkFunction;

    @Value("${fetch.all.extensions.phone.number}")
    private String allExtensionPhoneNumberFunction;

    @Value("${fetch.all.extensions.sipUrl}")
    private String allExtensionSipURLFunction;

    @Value("${fetch.all.virtual.numbers}")
    private String allVirtualNumbers;

    @Value("${fetch.all.couch.model.lite.name}")
    private String couchModelLiteName;

    @Value("${fetch.all.couch.model.lite}")
    private String allCouchModelLite;

    public String getAllDocumentFunction() {
        return allDocumentFunction;
    }

    public String getAllAgentFunction() {
        return allAgentFunction;
    }

    public String getBaseModelsByUserAndTypeFunction() {
        return baseModelsByUserAndTypeFunction;
    }

    public String getAllQueueFunction() {
        return allQueueFunction;
    }

    public String getAllExtensionFunction() {
        return allExtensionFunction;
    }

    public String getAllRecordingFunction() {
        return allRecordingFunction;
    }

    public String getAllPhoneNumbersFunction() {
        return allPhoneNumbers;
    }

    public String getAllExtensionGtalkFunction() {
        return allExtensionGtalkFunction;
    }

    public String getAllExtensionPhoneNumberFunction() {
        return allExtensionPhoneNumberFunction;
    }

    public String getAllExtensionSipURLFunction() {
        return allExtensionSipURLFunction;
    }

    public String getAllVirtualNumbersFunction(){
       return allVirtualNumbers;
    }

    public String getCouchModelLiteName() {
        return couchModelLiteName;
    }

    public String getAllCouchModelLite() {
        return allCouchModelLite;
    }
}
