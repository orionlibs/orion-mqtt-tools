package io.github.orionlibs.orion_mqtt_tools.broker.client;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.hivemq.client.mqtt.MqttGlobalPublishFilter;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import java.util.concurrent.CompletableFuture;

public class ConnectorFactory
{
    public Mqtt5BlockingClient newBlockingMQTTConnector(String brokerUrl, int port, String clientId)
    {
        Mqtt5BlockingClient client = Mqtt5Client.builder()
                        .identifier(clientId)
                        .serverHost(brokerUrl)
                        .serverPort(port)
                        .buildBlocking();
        client.connect();
        return client;
    }


    public Mqtt5AsyncClient newAsynchronousMQTTConnectorForPublisher(String brokerUrl, int port, String topic, String payload, String clientId)
    {
        Mqtt5AsyncClient client = Mqtt5Client.builder()
                        .identifier(clientId)
                        .serverHost(brokerUrl)
                        .serverPort(port)
                        .buildAsync();
        client.connect()
                        .thenCompose(connAck -> {
                            System.out.println("Successfully connected publisher!");
                            final Mqtt5Publish publish = Mqtt5Publish.builder()
                                            .topic(topic)
                                            .payload(payload.getBytes(UTF_8))
                                            .qos(MqttQos.EXACTLY_ONCE)
                                            .build();
                            final var future = client.publish(publish);
                            return CompletableFuture.allOf(future);
                        }).thenCompose(unused -> {
                            return client.disconnect();
                        }).exceptionally(throwable -> {
                            System.out.println("Something went wrong publisher!");
                            return null;
                        });
        return client;
    }


    public Mqtt5AsyncClient newAsynchronousMQTTConnectorForSubscriber(String brokerUrl, int port, String topic, MqttQos qualityOfServiceLevel, String clientId)
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
        return client;
    }


    public Mqtt5AsyncClient newAsynchronousMQTTConnectorForUnsubscriber(String brokerUrl, int port, String topic, String clientId)
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
        return client;
    }
}
