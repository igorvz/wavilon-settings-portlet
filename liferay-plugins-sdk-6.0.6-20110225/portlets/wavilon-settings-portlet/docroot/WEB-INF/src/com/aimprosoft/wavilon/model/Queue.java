package com.aimprosoft.wavilon.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(value = {"maxTime", "maxLength", "forwardToOnMaxTime", "forwardToOnMaxLength", "musicOnHold"}, ignoreUnknown = true)
public class Queue extends BaseModel {
    @JsonProperty("max_time")
    private int maxTime;

    @JsonProperty("max_tength")
    private int maxLength;

    @JsonProperty("forward_to_on_max_time")
    private String forwardToOnMaxTime;

    @JsonProperty("forward_to_on_max_length")
    private String forwardToOnMaxLength;

    @JsonProperty("music_on_hold")
    private String musicOnHold;

    public Queue() {
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

    public String getForwardToOnMaxTime() {
        return forwardToOnMaxTime;
    }

    public void setForwardToOnMaxTime(String forwardToOnMaxTime) {
        this.forwardToOnMaxTime = forwardToOnMaxTime;
    }

    public String getForwardToOnMaxLength() {
        return forwardToOnMaxLength;
    }

    public void setForwardToOnMaxLength(String forwardToOnMaxLength) {
        this.forwardToOnMaxLength = forwardToOnMaxLength;
    }

    public String getMusicOnHold() {
        return musicOnHold;
    }

    public void setMusicOnHold(String musicOnHold) {
        this.musicOnHold = musicOnHold;
    }
}


