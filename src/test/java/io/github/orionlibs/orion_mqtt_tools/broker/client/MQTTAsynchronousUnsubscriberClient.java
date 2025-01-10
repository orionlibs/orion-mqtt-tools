package io.github.orionlibs.orion_mqtt_tools.broker.client;

import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

public class MQTTAsynchronousUnsubscriberClient
{
    private Mqtt5AsyncClient client;


    public MQTTAsynchronousUnsubscriberClient(String brokerUrl, int port, String topic, String clientId)
    {
        Mqtt5AsyncClient client = Mqtt5Client.builder()
                        .identifier(clientId)
                        .serverHost(brokerUrl)
                        .serverPort(port)
                        .buildAsync();
        client.connect()
                        .thenCompose(connAck -> {
                            System.out.println("Successfully connected unsubscriber!");
                            return client.unsubscribeWith().topicFilter(topic).send();
                        }).thenRun(() -> {
                        }).exceptionally(throwable -> {
                            System.out.println("Something went wrong unsubscriber!");
                            return null;
                        });
    }


    public Mqtt5AsyncClient getClient()
    {
        return client;
    }
}
