package com.aimprosoft.wavilon.config;

import org.springframework.beans.factory.annotation.Value;

public class ApplicationProperties {

    @Value("${start.node.phone.number.name}")
    private String startNodePhoneNumberName;

   @Value("${start.node.phone.number.locator}")
    private String startNodePhoneNumberLocator;

   @Value("${start.node.queue.name}")
    private String startNodeQueueName;

   @Value("${start.node.queue.music.on.hold}")
    private String startNodeQueueMusicOnHold;

   @Value("${start.node.queue.max.time}")
    private Integer startNodeQueueMaxTime;

   @Value("${start.node.queue.max.length}")
    private Integer startNodeQueueMaxLength;

   @Value("${start.node.queue.forward.on.max.time}")
    private String startNodeQueueForwardOnMaxTime;

   @Value("${start.node.queue.forward.on.max.length}")
    private String startNodeQueueForwardOnMaxLength;

   @Value("${start.node.agent.name}")
    private String startNodeAgentName;

   @Value("${start.node.extension.name}")
    private String startNodeExtensionName;

   @Value("${start.node.extension.channel}")
    private String startNodeExtensionChannel;

   @Value("${start.node.extension.destination}")
    private String startNodeExtensionDestination;


    public ApplicationProperties() {
    }

    public String getStartNodePhoneNumberName() {
        return startNodePhoneNumberName;
    }

    public String getStartNodePhoneNumberLocator() {
        return startNodePhoneNumberLocator;
    }

    public String getStartNodeQueueName() {
        return startNodeQueueName;
    }

    public String getStartNodeQueueMusicOnHold() {
        return startNodeQueueMusicOnHold;
    }

    public Integer getStartNodeQueueMaxTime() {
        return startNodeQueueMaxTime;
    }

    public Integer getStartNodeQueueMaxLength() {
        return startNodeQueueMaxLength;
    }

    public String getStartNodeQueueForwardOnMaxTime() {
        return startNodeQueueForwardOnMaxTime;
    }

    public String getStartNodeQueueForwardOnMaxLength() {
        return startNodeQueueForwardOnMaxLength;
    }

    public String getStartNodeAgentName() {
        return startNodeAgentName;
    }

    public String getStartNodeExtensionName() {
        return startNodeExtensionName;
    }

    public String getStartNodeExtensionChannel() {
        return startNodeExtensionChannel;
    }

    public String getStartNodeExtensionDestination() {
        return startNodeExtensionDestination;
    }
}
