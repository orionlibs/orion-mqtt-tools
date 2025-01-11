package io.github.orionlibs.orion_mqtt_tools;

import com.hivemq.client.mqtt.mqtt5.exceptions.Mqtt5ConnAckException;
import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAck;
import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAckReasonCode;

public class AuthenticationTester
{
    private final MQTTClientAdapter client;


    public AuthenticationTester(MQTTClientAdapter client)
    {
        this.client = client;
    }


    public void testCredentials(String brokerUrl, int port, String clientId, String username, String password) throws Exception
    {
        Mqtt5ConnAck result = client.connect(brokerUrl, port, clientId, username, password);
        if(result.getReasonCode().getCode() == Mqtt5ConnAckReasonCode.NOT_AUTHORIZED.getCode())
        {
            throw new Mqtt5ConnAckException(result, "NOT_AUTHORIZED");
        }
        client.disconnect();
    }
    /*public void testCredentials(String brokerUrl, int port, String clientId, String username, String password) throws Exception
    {
        client.connect(brokerUrl, port, clientId, username, password);
        client.disconnect();
    }*/


    public void testCredentialsWithDelay(String brokerUrl, int port, String clientId, String username, String password, int delayInSeconds) throws Exception
    {
        testCredentials(brokerUrl, port, clientId, username, password);
        Utils.nonblockingDelay(delayInSeconds);
    }
}
