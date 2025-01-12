package io.github.orionlibs.orion_mqtt_tools;

public class AuthorizationTester
{
    private final MQTTClientAdapter client;


    public AuthorizationTester(MQTTClientAdapter client)
    {
        this.client = client;
    }


    public void testPublishAuthorization(String topic, byte[] payload) throws Exception
    {
        client.publish(topic, payload);
    }


    public void testPublishAuthorizationWithDelay(String topic, byte[] payload, int delayInSeconds) throws Exception
    {
        testPublishAuthorization(topic, payload);
        Utils.nonblockingDelay(delayInSeconds);
    }


    public void testSubscribeAuthorization(String topic, MQTTCMessageAdapter messageAdapter) throws Exception
    {
        client.subscribe(topic, messageAdapter);
    }


    public void testSubscribeAuthorizationWithDelay(String topic, MQTTCMessageAdapter messageAdapter, int delayInSeconds) throws Exception
    {
        testSubscribeAuthorization(topic, messageAdapter);
        Utils.nonblockingDelay(delayInSeconds);
    }


    public void testUnsubscribeAuthorization(String topic) throws Exception
    {
        client.unsubscribe(topic);
    }


    public void testUnsubscribeAuthorizationWithDelay(String topic, int delayInSeconds) throws Exception
    {
        testUnsubscribeAuthorization(topic);
        Utils.nonblockingDelay(delayInSeconds);
    }
}
