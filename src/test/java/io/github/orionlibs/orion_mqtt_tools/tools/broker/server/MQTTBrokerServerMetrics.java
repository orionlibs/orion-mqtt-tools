package io.github.orionlibs.orion_mqtt_tools.tools.broker.server;

import java.util.concurrent.atomic.AtomicInteger;

public class MQTTBrokerServerMetrics
{
    private AtomicInteger currentPublishersConnections;
    private AtomicInteger currentSubscribersConnections;


    public MQTTBrokerServerMetrics()
    {
        currentPublishersConnections = new AtomicInteger();
        currentSubscribersConnections = new AtomicInteger();
    }
}
