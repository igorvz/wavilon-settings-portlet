package com.aimprosoft.wavilon.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class Extension extends BaseModel{

    @JsonProperty("name")
    private String name;

    @JsonProperty("extensionType")
    private String extensionType;

    @JsonProperty("extensionId")
    private int extensionId;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("sipURL")
    private String sipURL;

    @JsonProperty("gTalk")
    private String gTalk;

    public Extension(){
    }

    @Override
    public String getEntityType() {
        return "extension";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtensionType() {
        return extensionType;
    }

    public void setExtensionType(String extensionType) {
        this.extensionType = extensionType;
    }

    public int getExtensionId() {
        return extensionId;
    }

    public void setExtensionId(int extensionId) {
        this.extensionId = extensionId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSipURL() {
        return sipURL;
    }

    public void setSipURL(String sipURL) {
        this.sipURL = sipURL;
    }

    public String getgTalk() {
        return gTalk;
    }

    public void setgTalk(String gTalk) {
        this.gTalk = gTalk;
    }
}
