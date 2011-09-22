package com.aimprosoft.wavilon.model;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;
import java.util.Set;

//@JsonIgnoreProperties(value = {}, ignoreUnknown = true)
public class Queue extends BaseModel {

    @JsonProperty("title")
    private String title;

    @JsonProperty("maxTime")
    private int maxTime;

    @JsonProperty("maxLength")
    private int maxLength;

    @JsonProperty("distinctionMaxTimeType")
    private String distinctionMaxTimeType;

    @JsonProperty("distinctionMaxTimeNode")
    private String distinctionMaxTimeNode;

    @JsonProperty("distinctionFullType")
    private String distinctionFullType;

    @JsonProperty("distinctionFullNode")
    private String distinctionFullNode;

    @JsonProperty("musicOnHold")
    private String musicOnHold;

    @JsonProperty("agents")
    private List<String> agents;


    public Queue() {
    }

    @Override
    public String getEntityType() {
        return "queue";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getDistinctionMaxTimeType() {
        return distinctionMaxTimeType;
    }

    public void setDistinctionMaxTimeType(String distinctionMaxTimeType) {
        this.distinctionMaxTimeType = distinctionMaxTimeType;
    }

    public String getDistinctionMaxTimeNode() {
        return distinctionMaxTimeNode;
    }

    public void setDistinctionMaxTimeNode(String distinctionMaxTimeNode) {
        this.distinctionMaxTimeNode = distinctionMaxTimeNode;
    }

    public String getDistinctionFullType() {
        return distinctionFullType;
    }

    public void setDistinctionFullType(String distinctionFullType) {
        this.distinctionFullType = distinctionFullType;
    }

    public String getDistinctionFullNode() {
        return distinctionFullNode;
    }

    public void setDistinctionFullNode(String distinctionFullNode) {
        this.distinctionFullNode = distinctionFullNode;
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
    public String toString() {
        return title;
    }



}


