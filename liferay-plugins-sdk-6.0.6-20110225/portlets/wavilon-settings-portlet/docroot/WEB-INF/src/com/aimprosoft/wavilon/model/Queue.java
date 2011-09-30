package com.aimprosoft.wavilon.model;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

//@JsonIgnoreProperties(value = {}, ignoreUnknown = true)
public class Queue extends BaseModel {

    @JsonProperty("name")
    private String name;

    @JsonProperty("maxTime")
    private int maxTime;

    @JsonProperty("maxLength")
    private int maxLength;

    @JsonProperty("extensionOnMaxTime")
    private String extensionOnMaxTime;

    @JsonProperty("extensionOnMaxLength")
    private String extensionOnMaxLength;

    @JsonProperty("musicOnHold")
    private String musicOnHold;

    @JsonProperty("agents")
    private List<String> agents;


    public Queue() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public String getExtensionOnMaxTime() {
        return extensionOnMaxTime;
    }

    public void setExtensionOnMaxTime(String extensionOnMaxTime) {
        this.extensionOnMaxTime = extensionOnMaxTime;
    }

    public String getExtensionOnMaxLength() {
        return extensionOnMaxLength;
    }

    public void setExtensionOnMaxLength(String extensionOnMaxLength) {
        this.extensionOnMaxLength = extensionOnMaxLength;
    }

    public String getMusicOnHold() {
        return musicOnHold;
    }

    public void setMusicOnHold(String musicOnHold) {
        this.musicOnHold = musicOnHold;
    }

    public List<String> getAgents() {
        return agents;
    }

    public void setAgents(List<String> agents) {
        this.agents = agents;
    }

    @Override
    public String getEntityType() {
        return "queue";
    }

    @Override
    public String toString() {
        return name;
    }


}


