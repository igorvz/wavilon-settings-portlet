package com.aimprosoft.wavilon.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class Extension extends BaseModel{
    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("sipURL")
    private String sipURL;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("extensionNumber")
    private int extensionNumber;

    public Extension(){

    }

    @Override
    public String getEntityType() {
        return "extension";
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSipURL() {
        return sipURL;
    }

    public void setSipURL(String sipURL) {
        this.sipURL = sipURL;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getExtensionNumber() {
        return extensionNumber;
    }

    public void setExtensionNumber(int extensionNumber) {
        this.extensionNumber = extensionNumber;
    }
}
