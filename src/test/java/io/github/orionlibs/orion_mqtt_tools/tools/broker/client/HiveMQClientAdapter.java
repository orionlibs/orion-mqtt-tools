package io.github.orionlibs.orion_mqtt_tools.tools.broker.client;

import com.hivemq.client.mqtt.MqttGlobalPublishFilter;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.message.auth.Mqtt5SimpleAuth;
import io.github.orionlibs.orion_mqtt_tools.MQTTClientAdapter;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

public class HiveMQClientAdapter implements MQTTClientAdapter
{
    private Mqtt5AsyncClient client;


    @Override
    public void connect(String brokerUrl, int port, String clientId, String username, String password) throws ExecutionException, InterruptedException
    {
        this.client = Mqtt5Client.builder()
                        .identifier(clientId)
                        .simpleAuth(Mqtt5SimpleAuth.builder().username(username).password(password.getBytes(StandardCharsets.UTF_8)).build())
                        .serverHost(brokerUrl)
                        .serverPort(port)
                        .buildAsync();
        client.connect();
    }


    @Override
    public void publish(String topic, byte[] payload)
    {
        client.publishWith().topic(topic).payload(payload).send();
    }


    @Override
    public void subscribe(String topic, MqttMessageHandler handler)
    {
        client.subscribeWith().topicFilter(topic).qos(MqttQos.EXACTLY_ONCE).send();
        client.publishes(MqttGlobalPublishFilter.SUBSCRIBED, publish -> {
            handler.handleMessage(topic, publish.getPayloadAsBytes());
        });
    }


    @Override
    public void disconnect()
    {
        if(client != null && client.getConfig().getState().isConnectedOrReconnect())
        {
            client.disconnect();
        }
    }
}
