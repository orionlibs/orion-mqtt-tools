package io.github.orionlibs.orion_mqtt_tools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import io.github.orionlibs.orion_mqtt_tools.tools.broker.client.ConnectorFactory;
import io.github.orionlibs.orion_mqtt_tools.tools.broker.client.MQTTAsynchronousSubscriberClient;
import io.github.orionlibs.orion_mqtt_tools.tools.broker.server.MQTTBrokerServer;
import io.github.orionlibs.orion_mqtt_tools.tools.broker.server.MQTTConnectionInterceptor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
//@Execution(ExecutionMode.CONCURRENT)
public class ConnectionPoliciesTest extends ATest
{
    private ListLogHandler listLogHandler;
    private MQTTBrokerServer brokerServer;
    private MQTTAsynchronousSubscriberClient testSubscriberClientWrapper;
    private MQTTBrokerServerMetrics brokerServerMetrics;


    @BeforeEach
    void setUp() throws Exception
    {
        listLogHandler = new ListLogHandler();
        MQTTConnectionInterceptor.addLogHandler(listLogHandler);
        brokerServer = new MQTTBrokerServer();
        brokerServer.startBroker(false, false);
        brokerServerMetrics = brokerServer.getBrokerServerMetrics();
        Utils.nonblockingDelay(3);
    }


    @AfterEach
    void teardown()
    {
        MQTTConnectionInterceptor.removeLogHandler(listLogHandler);
        brokerServer.stopBroker();
    }


    @Test
    void testPublisherDroppingMessages()
    {
        Mqtt5AsyncClient testPublisherClient1 = startPublisherClient("publisherClient1");
        Utils.nonblockingDelay(2);
        Mqtt5AsyncClient testPublisherClient2 = startPublisherClient("publisherClient2");
        Utils.nonblockingDelay(2);
        Mqtt5AsyncClient testPublisherClient3 = startPublisherClient("publisherClient3");
        Utils.nonblockingDelay(2);
        assertEquals(1, listLogHandler.getLogRecords().size());
        assertTrue(listLogHandler.getLogRecords()
                        .stream()
                        .anyMatch(record -> record.getMessage().contains("exceeded the maximum number of publishers allowed")));
        String topic = "test/topic1";
        Mqtt5AsyncClient testSubscriberClient1 = startSubscriberClient(topic, MqttQos.EXACTLY_ONCE, "subscriberClient1");
        Utils.nonblockingDelay(2);
        Mqtt5AsyncClient testSubscriberClient2 = startSubscriberClient(topic, MqttQos.EXACTLY_ONCE, "subscriberClient2");
        Utils.nonblockingDelay(2);
        Mqtt5AsyncClient testSubscriberClient3 = startSubscriberClient(topic, MqttQos.EXACTLY_ONCE, "subscriberClient3");
        Utils.nonblockingDelay(2);
        assertEquals(2, listLogHandler.getLogRecords().size());
        assertTrue(listLogHandler.getLogRecords()
                        .stream()
                        .anyMatch(record -> record.getMessage().contains("exceeded the maximum number of subscribers allowed")));
        if(testPublisherClient1 != null && testPublisherClient1.getConfig().getState().isConnectedOrReconnect())
        {
            testPublisherClient1.disconnect();
        }
        if(testPublisherClient2 != null && testPublisherClient2.getConfig().getState().isConnectedOrReconnect())
        {
            testPublisherClient2.disconnect();
        }
        if(testPublisherClient3 != null && testPublisherClient3.getConfig().getState().isConnectedOrReconnect())
        {
            testPublisherClient3.disconnect();
        }
        if(testSubscriberClient1 != null && testSubscriberClient1.getConfig().getState().isConnectedOrReconnect())
        {
            testSubscriberClient1.disconnect();
        }
        if(testSubscriberClient2 != null && testSubscriberClient2.getConfig().getState().isConnectedOrReconnect())
        {
            testSubscriberClient2.disconnect();
        }
        if(testSubscriberClient3 != null && testSubscriberClient3.getConfig().getState().isConnectedOrReconnect())
        {
            testSubscriberClient3.disconnect();
        }
    }


    private Mqtt5AsyncClient startPublisherClient(String clientId)
    {
        return new ConnectorFactory().newAsynchronousMQTTConnectorForPublisher("0.0.0.0", 1883, clientId, brokerServerMetrics).getClient();
    }


    private Mqtt5AsyncClient startSubscriberClient(String topic, MqttQos qualityOfServiceLevel, String clientId)
    {
        testSubscriberClientWrapper = new ConnectorFactory().newAsynchronousMQTTConnectorForSubscriber("0.0.0.0", 1883, topic, qualityOfServiceLevel, clientId, brokerServerMetrics);
        return testSubscriberClientWrapper.getClient();
    }
}
