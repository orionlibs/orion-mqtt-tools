package io.github.orionlibs.orion_mqtt_tools.tools.broker.client;

import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import io.github.orionlibs.orion_mqtt_tools.MQTTBrokerServerMetrics;

public class MQTTBlockingClient
{
    private Mqtt5BlockingClient client;
    private MQTTBrokerServerMetrics brokerServerMetrics;


    public MQTTBlockingClient(String brokerUrl, int port, String clientId, MQTTBrokerServerMetrics brokerServerMetrics)
    {
        this.brokerServerMetrics = brokerServerMetrics;
        Mqtt5BlockingClient client = Mqtt5Client.builder()
                        .identifier(clientId)
                        .serverHost(brokerUrl)
                        .serverPort(port)
                        .buildBlocking();
        client.connect();
        if(client.getState().isConnected())
        {
            brokerServerMetrics.incrementNumberOfAllConnections();
        }
    }


    public Mqtt5BlockingClient getClient()
    {
        return client;
    }
}
