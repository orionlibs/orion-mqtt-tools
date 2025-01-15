package io.github.orionlibs.orion_mqtt_tools;

public class AuthenticationTester
{
    private final MQTTClientAdapter client;
    private MQTTBrokerServerMetrics brokerServerMetrics;


    public AuthenticationTester(MQTTClientAdapter client, MQTTBrokerServerMetrics brokerServerMetrics)
    {
        this.client = client;
        this.brokerServerMetrics = brokerServerMetrics;
    }


    public void testCredentials(String brokerUrl, int port, String clientId, String username, String password) throws Exception
    {
        client.connect(brokerUrl, port, clientId, username, password, brokerServerMetrics);
        client.disconnect();
    }


    public void testCredentialsWithDelay(String brokerUrl, int port, String clientId, String username, String password, int delayInSeconds) throws Exception
    {
        testCredentials(brokerUrl, port, clientId, username, password);
        Utils.nonblockingDelay(delayInSeconds);
    }
}
