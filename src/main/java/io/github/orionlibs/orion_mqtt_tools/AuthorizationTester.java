package io.github.orionlibs.orion_mqtt_tools;

public class AuthorizationTester
{
    private final MQTTClientAdapter client;


    public AuthorizationTester(MQTTClientAdapter client)
    {
        this.client = client;
    }


    public boolean testPublishAuthorization(String brokerUrl, int port, String clientId, String username, String password, String topic, byte[] payload)
    {
        try
        {
            client.connect(brokerUrl, port, clientId, username, password);
            client.publish(topic, payload);
            client.disconnect();
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }


    public boolean testSubscribeAuthorization(String brokerUrl, int port, String clientId, String username, String password, String topic)
    {
        try
        {
            client.connect(brokerUrl, port, clientId, username, password);
            client.subscribe(topic, (t, p) -> {
            });
            client.disconnect();
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }
}
