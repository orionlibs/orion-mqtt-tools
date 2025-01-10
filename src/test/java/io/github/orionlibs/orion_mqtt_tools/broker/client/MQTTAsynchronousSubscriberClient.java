package io.github.orionlibs.orion_mqtt_tools.broker.client;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.hivemq.client.mqtt.MqttGlobalPublishFilter;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

public class MQTTAsynchronousSubscriberClient
{
    private Mqtt5AsyncClient client;


    public MQTTAsynchronousSubscriberClient(String brokerUrl, int port, String topic, MqttQos qualityOfServiceLevel, String clientId)
    {
        Mqtt5AsyncClient client = Mqtt5Client.builder()
                        .identifier(clientId)
                        .serverHost(brokerUrl)
                        .serverPort(port)
                        .buildAsync();
        client.publishes(MqttGlobalPublishFilter.SUBSCRIBED, publish -> {
            System.out.println("Received payload: " + new String(publish.getPayloadAsBytes(), UTF_8));
        });
        client.connect()
                        .thenCompose(connAck -> {
                            System.out.println("Successfully connected subscriber!");
                            return client.subscribeWith().topicFilter(topic).qos(qualityOfServiceLevel).send();
                        }).thenRun(() -> {
                        }).exceptionally(throwable -> {
                            System.out.println("Something went wrong subscriber!");
                            return null;
                        });
    }


    public Mqtt5AsyncClient getClient()
    {
        return client;
    }
}