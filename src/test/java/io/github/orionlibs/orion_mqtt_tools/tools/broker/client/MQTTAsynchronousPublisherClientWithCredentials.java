package io.github.orionlibs.orion_mqtt_tools.tools.broker.client;

import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.message.auth.Mqtt5SimpleAuth;
import com.hivemq.client.mqtt.mqtt5.message.connect.Mqtt5Connect;
import io.github.orionlibs.orion_mqtt_tools.MQTTBrokerServerMetrics;
import io.github.orionlibs.orion_mqtt_tools.MQTTClientType;
import io.github.orionlibs.orion_mqtt_tools.MQTTUserProperties;
import java.nio.charset.StandardCharsets;

public class MQTTAsynchronousPublisherClientWithCredentials
{
    private Mqtt5AsyncClient client;
    private MQTTBrokerServerMetrics brokerServerMetrics;


    public MQTTAsynchronousPublisherClientWithCredentials(String brokerUrl, int port, String clientId, String username, String password, MQTTBrokerServerMetrics brokerServerMetrics)
    {
        this.brokerServerMetrics = brokerServerMetrics;
        this.client = Mqtt5Client.builder()
                        .identifier(clientId)
                        .simpleAuth(Mqtt5SimpleAuth.builder()
                                        .username(username)
                                        .password(password.getBytes(StandardCharsets.UTF_8))
                                        .build())
                        .serverHost(brokerUrl)
                        .serverPort(port)
                        .buildAsync();
        Mqtt5Connect connectMessage = Mqtt5Connect.builder()
                        .userProperties()
                        .add(MQTTUserProperties.CLIENT_TYPE, MQTTClientType.PUBLISHER.get())
                        .applyUserProperties()
                        .build();
        client.connect(connectMessage).thenRun(() -> {
            System.out.println("Successfully connected publisher!");
            brokerServerMetrics.incrementNumberOfPublishersConnections();
        });
    }


    public Mqtt5AsyncClient getClient()
    {
        return client;
    }
}
