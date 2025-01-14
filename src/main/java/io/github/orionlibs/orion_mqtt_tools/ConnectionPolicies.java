package io.github.orionlibs.orion_mqtt_tools;

public class ConnectionPolicies
{
    private MQTTBrokerConfiguration brokerConfiguration;


    public ConnectionPolicies(MQTTBrokerConfiguration brokerConfiguration)
    {
        this.brokerConfiguration = brokerConfiguration;
    }


    public boolean allowNewPublisherConnection(int currentNumberOfPublishersConnections)
    {
        return currentNumberOfPublishersConnections + 1 <= brokerConfiguration.getMaximumNumberOfAllowedPublishersConnections();
    }


    public boolean allowNewSubscriberConnection(int currentNumberOfSubscribersConnections)
    {
        return currentNumberOfSubscribersConnections + 1 <= brokerConfiguration.getMaximumNumberOfAllowedSubscribersConnections();
    }
}
