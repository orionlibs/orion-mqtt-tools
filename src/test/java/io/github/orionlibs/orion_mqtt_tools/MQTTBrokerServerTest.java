package io.github.orionlibs.orion_mqtt_tools;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import io.github.orionlibs.orion_mqtt_tools.tools.broker.client.ConnectorFactory;
import io.github.orionlibs.orion_mqtt_tools.tools.broker.server.MQTTBrokerServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
//@Execution(ExecutionMode.CONCURRENT)
public class MQTTBrokerServerTest extends ATest
{
    private MQTTBrokerServer brokerServer;
    private Mqtt5AsyncClient testPublisherClient;
    private Mqtt5AsyncClient testSubscriberClient;
    private Mqtt5AsyncClient testUnsubscriberClient;
    private String clientID = "testClientId";
    private MQTTBrokerServerMetrics brokerServerMetrics;


    @BeforeEach
    void setUp() throws Exception
    {
        brokerServer = new MQTTBrokerServer();
        brokerServer.startBroker(false, false);
        brokerServerMetrics = brokerServer.getBrokerServerMetrics();
        Utils.nonblockingDelay(3);
    }


    @AfterEach
    void teardown()
    {
        if(testPublisherClient != null && testPublisherClient.getConfig().getState().isConnectedOrReconnect())
        {
            testPublisherClient.disconnect();
        }
        if(testSubscriberClient != null && testSubscriberClient.getConfig().getState().isConnectedOrReconnect())
        {
            testSubscriberClient.disconnect();
        }
        if(testUnsubscriberClient != null && testUnsubscriberClient.getConfig().getState().isConnectedOrReconnect())
        {
            testUnsubscriberClient.disconnect();
        }
        brokerServer.stopBroker();
    }


    /*@Test
    void testBrokerStartup()
    {
        assertTrue(brokerServer.isRunning(), "Broker should be running after startup");
    }*/


    @Test
    void testPublishAndSubscribeAndUnsubscribeAndPersistenceAfterMQTTServerShutdown() throws InterruptedException
    {
        startSubscriberClient("test/topic1", MqttQos.EXACTLY_ONCE, clientID);
        Thread.sleep(4000L);
        startPublisherClient("test/topic1", "somePayload1", "testPublisherId");
        Thread.sleep(2000L);
        startPublisherClient("test/topic1", "somePayload2", "testPublisherId");
        Thread.sleep(2000L);
        startPublisherClient("test/topic1", "somePayload3", "testPublisherId");
        Thread.sleep(2000L);
        startUnsubscriberClient("test/topic1", clientID);
        Thread.sleep(2000L);
        startPublisherClient("test/topic1", "somePayload4", "testPublisherId");
        Thread.sleep(2000L);
    }


    private void startPublisherClient(String topic, String payload, String clientId)
    {
        this.testPublisherClient = new ConnectorFactory().newAsynchronousMQTTConnectorForPublisher("0.0.0.0", 1883, topic, payload, clientId).getClient();
    }


    private void startSubscriberClient(String topic, MqttQos qualityOfServiceLevel, String clientId)
    {
        this.testSubscriberClient = new ConnectorFactory().newAsynchronousMQTTConnectorForSubscriber("0.0.0.0", 1883, topic, qualityOfServiceLevel, clientId, brokerServerMetrics).getClient();
    }


    private void startUnsubscriberClient(String topic, String clientId)
    {
        this.testUnsubscriberClient = new ConnectorFactory().newAsynchronousMQTTConnectorForUnsubscriber("0.0.0.0", 1883, topic, clientId, brokerServerMetrics).getClient();
    }
}
