package io.github.orionlibs.orion_mqtt_tools.tools.broker.client;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.hivemq.client.mqtt.MqttGlobalPublishFilter;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.message.connect.Mqtt5Connect;
import io.github.orionlibs.orion_mqtt_tools.MQTTBrokerServerMetrics;
import io.github.orionlibs.orion_mqtt_tools.MQTTClientType;
import io.github.orionlibs.orion_mqtt_tools.MQTTUserProperties;

public class MQTTAsynchronousSubscriberClient
{
    private Mqtt5AsyncClient client;
    private MQTTBrokerServerMetrics brokerServerMetrics;
    private int numberOfMessagesReceived;


    public MQTTAsynchronousSubscriberClient(String brokerUrl, int port, String topic, MqttQos qualityOfServiceLevel, String clientId, MQTTBrokerServerMetrics brokerServerMetrics)
    {
        this.brokerServerMetrics = brokerServerMetrics;
        numberOfMessagesReceived = 0;
        this.client = Mqtt5Client.builder()
                        .identifier(clientId)
                        .serverHost(brokerUrl)
                        .serverPort(port)
                        .buildAsync();
        client.publishes(MqttGlobalPublishFilter.SUBSCRIBED, publish -> {
            numberOfMessagesReceived++;
            System.out.println("Received payload: " + new String(publish.getPayloadAsBytes(), UTF_8));
        });
        Mqtt5Connect connectMessage = Mqtt5Connect.builder()
                        .userProperties()
                        .add(MQTTUserProperties.CLIENT_TYPE, MQTTClientType.SUBSCRIBER.get())
                        .applyUserProperties()
                        .build();
        client.connect(connectMessage)
                        .thenCompose(connAck -> {
                            System.out.println("Successfully connected subscriber!");
                            brokerServerMetrics.incrementNumberOfSubscribersConnections();
                            return client.subscribeWith().topicFilter(topic).qos(qualityOfServiceLevel).send();
                        }).exceptionally(throwable -> {
                            System.out.println("Something went wrong subscriber: " + throwable.getMessage());
                            return null;
                        });
    }


    public Mqtt5AsyncClient getClient()
    {
        return client;
    }


    public int getNumberOfMessagesReceived()
    {
        return numberOfMessagesReceived;
    }
}
