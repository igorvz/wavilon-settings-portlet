package com.aimprosoft.wavilon.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class Extension extends BaseModel{
    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("url")
    private String url;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("extensionNumber")
    private int extensionNumber;

    public Extension(){

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
