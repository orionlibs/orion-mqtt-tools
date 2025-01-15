package io.github.orionlibs.orion_mqtt_tools.tools.broker.client;

import com.hivemq.client.mqtt.MqttGlobalPublishFilter;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import io.github.orionlibs.orion_mqtt_tools.MQTTBrokerServerMetrics;
import io.github.orionlibs.orion_mqtt_tools.MQTTClientAdapter;

public class HiveMQClientAdapter implements MQTTClientAdapter
{
    private Mqtt5AsyncClient client;


    public HiveMQClientAdapter()
    {
    }


    public HiveMQClientAdapter(String brokerUrl, int port, String clientId, String username, String password, MQTTBrokerServerMetrics brokerServerMetrics)
    {
        connect(brokerUrl, port, clientId, username, password, brokerServerMetrics);
    }


    @Override
    public void connect(String brokerUrl, int port, String clientId, String username, String password, MQTTBrokerServerMetrics brokerServerMetrics)
    {
        this.client = new ConnectorFactory().newAsynchronousMQTTConnectorForPublisherWithCredentials(brokerUrl, port, clientId, username, password, brokerServerMetrics).getClient();
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
    public void unsubscribe(String topic)
    {
        client.unsubscribeWith().topicFilter(topic).send();
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
