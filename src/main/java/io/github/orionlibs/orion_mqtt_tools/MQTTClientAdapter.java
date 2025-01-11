package io.github.orionlibs.orion_mqtt_tools;

import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAck;

public interface MQTTClientAdapter
{
    Mqtt5ConnAck connect(String brokerUrl, int port, String clientId, String username, String password) throws Exception;


    void publish(String topic, byte[] payload) throws Exception;


    void subscribe(String topic, MqttMessageHandler handler) throws Exception;


    void disconnect() throws Exception;


    interface MqttMessageHandler
    {
        void handleMessage(String topic, byte[] payload);
    }
}
