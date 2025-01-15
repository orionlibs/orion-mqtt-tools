package io.github.orionlibs.orion_mqtt_tools;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MQTTBrokerConfiguration
{
    private int maximumNumberOfAllowedPublishersConnections;
    private int maximumNumberOfAllowedSubscribersConnections;
    private int maximumNumberOfAllowedConnections;
}
