package io.github.orionlibs.orion_mqtt_tools;

public interface MQTTClientAdapter
{
    void connect(String brokerUrl, int port, String clientId, String username, String password) throws Exception;


    void publish(String topic, byte[] payload) throws Exception;


    void subscribe(String topic, MqttMessageHandler handler) throws Exception;


    void unsubscribe(String topic) throws Exception;


    void disconnect() throws Exception;


    interface MqttMessageHandler
    {
        void handleMessage(String topic, byte[] payload);
    }
}
