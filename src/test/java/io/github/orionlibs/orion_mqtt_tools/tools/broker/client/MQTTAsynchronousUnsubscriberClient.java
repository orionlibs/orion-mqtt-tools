package io.github.orionlibs.orion_mqtt_tools.tools.broker.client;

import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.message.connect.Mqtt5Connect;
import io.github.orionlibs.orion_mqtt_tools.MQTTBrokerServerMetrics;
import io.github.orionlibs.orion_mqtt_tools.MQTTClientType;
import io.github.orionlibs.orion_mqtt_tools.MQTTUserProperties;

public class MQTTAsynchronousUnsubscriberClient
{
    private Mqtt5AsyncClient client;
    private MQTTBrokerServerMetrics brokerServerMetrics;


    public MQTTAsynchronousUnsubscriberClient(String brokerUrl, int port, String topic, String clientId, MQTTBrokerServerMetrics brokerServerMetrics)
    {
        this.brokerServerMetrics = brokerServerMetrics;
        this.client = Mqtt5Client.builder()
                        .identifier(clientId)
                        .serverHost(brokerUrl)
                        .serverPort(port)
                        .buildAsync();
        Mqtt5Connect connectMessage = Mqtt5Connect.builder()
                        .userProperties()
                        .add(MQTTUserProperties.CLIENT_TYPE, MQTTClientType.UNSUBSCRIBER.get())
                        .applyUserProperties()
                        .build();
        client.connect(connectMessage)
                        .thenCompose(connAck -> {
                            System.out.println("Successfully connected unsubscriber!");
                            brokerServerMetrics.incrementNumberOfAllConnections();
                            return client.unsubscribeWith().topicFilter(topic).send();
                        }).exceptionally(throwable -> {
                            System.out.println("Something went wrong unsubscriber: " + throwable.getMessage());
                            return null;
                        });
    }


    public Mqtt5AsyncClient getClient()
    {
        return client;
    }
}
