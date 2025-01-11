package io.github.orionlibs.orion_mqtt_tools;

public class AuthorizationTester
{
    private final MQTTClientAdapter client;


    public AuthorizationTester(MQTTClientAdapter client)
    {
        this.client = client;
    }


    public void testPublishAuthorization(String brokerUrl, int port, String clientId, String username, String password, String topic, byte[] payload) throws Exception
    {
        client.connect(brokerUrl, port, clientId, username, password);
        client.publish(topic, payload);
        client.disconnect();
    }


    public void testPublishAuthorizationWithDelay(String brokerUrl, int port, String clientId, String username, String password, String topic, byte[] payload, int delayInSeconds) throws Exception
    {
        testPublishAuthorization(brokerUrl, port, clientId, username, password, topic, payload);
        Utils.nonblockingDelay(delayInSeconds);
    }


    public void testSubscribeAuthorization(String brokerUrl, int port, String clientId, String username, String password, String topic, MQTTCMessageAdapter messageAdapter) throws Exception
    {
        client.connect(brokerUrl, port, clientId, username, password);
        client.subscribe(topic, messageAdapter);
        client.disconnect();
    }


    public void testSubscribeAuthorizationWithDelay(String brokerUrl, int port, String clientId, String username, String password, String topic, MQTTCMessageAdapter messageAdapter, int delayInSeconds) throws Exception
    {
        testSubscribeAuthorization(brokerUrl, port, clientId, username, password, topic, messageAdapter);
        Utils.nonblockingDelay(delayInSeconds);
    }
}
