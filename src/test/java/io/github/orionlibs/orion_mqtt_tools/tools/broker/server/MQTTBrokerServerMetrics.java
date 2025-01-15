package io.github.orionlibs.orion_mqtt_tools.tools.broker.server;

import java.util.concurrent.atomic.AtomicInteger;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MQTTBrokerServerMetrics
{
    private AtomicInteger currentPublishersConnections;
    private AtomicInteger currentSubscribersConnections;


    public MQTTBrokerServerMetrics()
    {
        currentPublishersConnections = new AtomicInteger();
        currentSubscribersConnections = new AtomicInteger();
    }


    public void incrementNumberOfPublishersConnections()
    {
        currentPublishersConnections.incrementAndGet();
    }


    public void incrementNumberOfSubscribersConnections()
    {
        currentSubscribersConnections.incrementAndGet();
    }
}
