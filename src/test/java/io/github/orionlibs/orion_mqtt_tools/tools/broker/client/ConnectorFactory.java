package io.github.orionlibs.orion_mqtt_tools.tools.broker.client;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import io.github.orionlibs.orion_mqtt_tools.MQTTBrokerServerMetrics;

public class ConnectorFactory
{
    public MQTTBlockingClient newBlockingMQTTConnector(String brokerUrl, int port, String clientId, MQTTBrokerServerMetrics brokerServerMetrics)
    {
        return new MQTTBlockingClient(brokerUrl, port, clientId, brokerServerMetrics);
    }


    public MQTTAsynchronousPublisherClient newAsynchronousMQTTConnectorForPublisher(String brokerUrl, int port, String topic, String payload, String clientId)
    {
        return new MQTTAsynchronousPublisherClient(brokerUrl, port, topic, payload, clientId);
    }


    public MQTTAsynchronousPublisherClient newAsynchronousMQTTConnectorForPublisher(String brokerUrl, int port, String clientId, MQTTBrokerServerMetrics brokerServerMetrics)
    {
        return new MQTTAsynchronousPublisherClient(brokerUrl, port, clientId, brokerServerMetrics);
    }


    public MQTTAsynchronousPublisherClientWithCredentials newAsynchronousMQTTConnectorForPublisherWithCredentials(String brokerUrl, int port, String clientId, String username, String password, MQTTBrokerServerMetrics brokerServerMetrics)
    {
        return new MQTTAsynchronousPublisherClientWithCredentials(brokerUrl, port, clientId, username, password, brokerServerMetrics);
    }


    public MQTTAsynchronousSubscriberClient newAsynchronousMQTTConnectorForSubscriber(String brokerUrl, int port, String topic, MqttQos qualityOfServiceLevel, String clientId, MQTTBrokerServerMetrics brokerServerMetrics)
    {
        return new MQTTAsynchronousSubscriberClient(brokerUrl, port, topic, qualityOfServiceLevel, clientId, brokerServerMetrics);
    }


    public MQTTAsynchronousUnsubscriberClient newAsynchronousMQTTConnectorForUnsubscriber(String brokerUrl, int port, String topic, String clientId, MQTTBrokerServerMetrics brokerServerMetrics)
    {
        return new MQTTAsynchronousUnsubscriberClient(brokerUrl, port, topic, clientId, brokerServerMetrics);
    }
}
