package io.github.orionlibs.orion_mqtt_tools;

public class AuthenticationTester
{
    private final MQTTClientAdapter client;


    public AuthenticationTester(MQTTClientAdapter client)
    {
        this.client = client;
    }


    public boolean testCredentials(String brokerUrl, int port, String username, String password)
    {
        try
        {
            client.connect(brokerUrl, port, username, password);
            client.disconnect();
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }
}
