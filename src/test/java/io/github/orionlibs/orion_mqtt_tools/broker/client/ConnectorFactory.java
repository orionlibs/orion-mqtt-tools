package io.github.orionlibs.orion_mqtt_tools.broker.client;

import com.hivemq.client.mqtt.datatypes.MqttQos;

public class ConnectorFactory
{
    public MQTTBlockingClient newBlockingMQTTConnector(String brokerUrl, int port, String clientId)
    {
        return new MQTTBlockingClient(brokerUrl, port, clientId);
    }


    public MQTTAsynchronousPublisherClient newAsynchronousMQTTConnectorForPublisher(String brokerUrl, int port, String topic, String payload, String clientId)
    {
        return new MQTTAsynchronousPublisherClient(brokerUrl, port, topic, payload, clientId);
    }


    public MQTTAsynchronousSubscriberClient newAsynchronousMQTTConnectorForSubscriber(String brokerUrl, int port, String topic, MqttQos qualityOfServiceLevel, String clientId)
    {
        return new MQTTAsynchronousSubscriberClient(brokerUrl, port, topic, qualityOfServiceLevel, clientId);
    }


    public MQTTAsynchronousUnsubscriberClient newAsynchronousMQTTConnectorForUnsubscriber(String brokerUrl, int port, String topic, String clientId)
    {
        return new MQTTAsynchronousUnsubscriberClient(brokerUrl, port, topic, clientId);
    }
}
