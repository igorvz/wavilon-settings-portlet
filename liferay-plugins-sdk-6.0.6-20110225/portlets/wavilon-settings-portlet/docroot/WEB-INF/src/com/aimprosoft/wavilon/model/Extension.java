package com.aimprosoft.wavilon.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(value = {"jumpIfBusy", "jumpIfNoAnswer"}, ignoreUnknown = true)
public class Extension extends BaseModel{

    @JsonProperty("channel")
    private String channel;

    @JsonProperty("destination")
    private String destination;

    @JsonProperty("code")
    private Integer code;

    @JsonProperty("jump_if_busy")
    private String  jumpIfBusy;

    @JsonProperty("jump_if_no_answer")
    private String  jumpIfNoAnswer;

    public Extension(){
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getJumpIfBusy() {
        return jumpIfBusy;
    }

    public void setJumpIfBusy(String jumpIfBusy) {
        this.jumpIfBusy = jumpIfBusy;
    }

    public String getJumpIfNoAnswer() {
        return jumpIfNoAnswer;
    }

    public void setJumpIfNoAnswer(String jumpIfNoAnswer) {
        this.jumpIfNoAnswer = jumpIfNoAnswer;
    }
}
