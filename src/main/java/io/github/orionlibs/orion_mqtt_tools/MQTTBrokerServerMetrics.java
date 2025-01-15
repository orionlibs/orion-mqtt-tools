package io.github.orionlibs.orion_mqtt_tools;

import java.util.concurrent.atomic.AtomicInteger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class MQTTBrokerServerMetrics
{
    private AtomicInteger currentNumberOfPublishersConnections;
    private AtomicInteger currentNumberOfSubscribersConnections;
    private AtomicInteger currentNumberOfAllConnections;


    public MQTTBrokerServerMetrics()
    {
        currentNumberOfPublishersConnections = new AtomicInteger();
        currentNumberOfSubscribersConnections = new AtomicInteger();
        currentNumberOfAllConnections = new AtomicInteger();
    }


    public void incrementNumberOfPublishersConnections()
    {
        currentNumberOfPublishersConnections.incrementAndGet();
        currentNumberOfAllConnections.incrementAndGet();
    }


    public void incrementNumberOfSubscribersConnections()
    {
        currentNumberOfSubscribersConnections.incrementAndGet();
        currentNumberOfAllConnections.incrementAndGet();
    }


    public void incrementNumberOfAllConnections()
    {
        currentNumberOfAllConnections.incrementAndGet();
    }
}
