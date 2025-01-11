package io.github.orionlibs.orion_mqtt_tools;

public interface MQTTClientAdapter
{
    void connect(String brokerUrl, String username, String password) throws Exception;


    void publish(String topic, byte[] payload) throws Exception;


    void subscribe(String topic, MqttMessageHandler handler) throws Exception;


    void disconnect() throws Exception;


    interface MqttMessageHandler
    {
        void handleMessage(String topic, byte[] payload);
    }
}
