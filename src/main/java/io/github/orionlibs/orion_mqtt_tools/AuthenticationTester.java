package io.github.orionlibs.orion_mqtt_tools;

public class AuthenticationTester
{
    private final MQTTClientAdapter client;


    public AuthenticationTester(MQTTClientAdapter client)
    {
        this.client = client;
    }


    public void testCredentials(String brokerUrl, int port, String clientId, String username, String password) throws Exception
    {
        client.connect(brokerUrl, port, clientId, username, password);
        client.disconnect();
    }


    public void testCredentialsWithDelay(String brokerUrl, int port, String clientId, String username, String password, int delayInSeconds) throws Exception
    {
        testCredentials(brokerUrl, port, clientId, username, password);
        Utils.nonblockingDelay(delayInSeconds);
    }
}
