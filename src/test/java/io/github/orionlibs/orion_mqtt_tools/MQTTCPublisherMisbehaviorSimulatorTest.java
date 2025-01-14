package io.github.orionlibs.orion_mqtt_tools;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishResult;
import io.github.orionlibs.orion_mqtt_tools.tools.broker.client.ConnectorFactory;
import io.github.orionlibs.orion_mqtt_tools.tools.broker.client.MQTTAsynchronousSubscriberClient;
import io.github.orionlibs.orion_mqtt_tools.tools.broker.server.MQTTBrokerServer;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
//@Execution(ExecutionMode.CONCURRENT)
public class MQTTCPublisherMisbehaviorSimulatorTest extends ATest
{
    private MQTTBrokerServer brokerServer;
    private Mqtt5AsyncClient testPublisherClient;
    private Mqtt5AsyncClient testSubscriberClient;
    private MQTTAsynchronousSubscriberClient testSubscriberClientWrapper;
    private String clientID = "testClientId";
    private MessageResiliencySimulationConfiguration resiliencyConfig;
    private MQTTClientDelaySimulator simulator;
    private MQTTBrokerServerMetrics brokerServerMetrics;


    @BeforeEach
    void setUp() throws Exception
    {
        brokerServer = new MQTTBrokerServer();
        brokerServer.startBroker(false, false);
        brokerServerMetrics = brokerServer.getBrokerServerMetrics();
        Utils.nonblockingDelay(3);
        resiliencyConfig = new MessageResiliencySimulationConfiguration(
                        1.0d,   //100% probability for delay
                        3000,  //max delay of 3s
                        1.0d,   //100% probability to drop message
                        1.0d   //100% probability for duplication
        );
        simulator = new MQTTClientDelaySimulator(resiliencyConfig);
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
        brokerServer.stopBroker();
    }


    @Test
    void testPublisherDroppingMessages() throws InterruptedException
    {
        String topic = "test/topic1";
        startSubscriberClient(topic, MqttQos.EXACTLY_ONCE, clientID);
        Thread.sleep(4000L);
        startPublisherClient("testPublisherId");
        Thread.sleep(2000L);
        publishSimulatingMessageDrop(topic, "somePayload2");
        Thread.sleep(500L);
        publishSimulatingMessageDrop(topic, "somePayload3");
        Thread.sleep(500L);
        publishSimulatingMessageDrop(topic, "somePayload4");
        Thread.sleep(500L);
        publishSimulatingMessageDrop(topic, "somePayload5");
        assertEquals(0, testSubscriberClientWrapper.getNumberOfMessagesReceived());
    }


    @Test
    void testPublisherPublishingDuplicateMessages() throws InterruptedException
    {
        String topic = "test/topic1";
        startSubscriberClient(topic, MqttQos.EXACTLY_ONCE, clientID);
        Thread.sleep(4000L);
        startPublisherClient("testPublisherId");
        Thread.sleep(2000L);
        simulator.publishSimulatingDuplicateMessage(() -> {
            CompletableFuture<Mqtt5PublishResult> future = testPublisherClient.publish(Mqtt5Publish.builder()
                            .topic(topic)
                            .payload("somePayload2".getBytes(UTF_8))
                            .qos(MqttQos.EXACTLY_ONCE)
                            .build());
            return future;
        });
        Thread.sleep(2000L);
        assertEquals(2, testSubscriberClientWrapper.getNumberOfMessagesReceived());
    }


    @Test
    void testPublisherWithDelay() throws InterruptedException
    {
        String topic = "test/topic1";
        startSubscriberClient(topic, MqttQos.EXACTLY_ONCE, clientID);
        Thread.sleep(4000L);
        startPublisherClient("testPublisherId");
        Thread.sleep(2000L);
        simulator.publishSimulatingDelay(() -> {
            CompletableFuture<Mqtt5PublishResult> future = testPublisherClient.publish(Mqtt5Publish.builder()
                            .topic(topic)
                            .payload("somePayload2".getBytes(UTF_8))
                            .qos(MqttQos.EXACTLY_ONCE)
                            .build());
            return future;
        });
        assertEquals(0, testSubscriberClientWrapper.getNumberOfMessagesReceived());
        Thread.sleep(5000L);
        assertEquals(1, testSubscriberClientWrapper.getNumberOfMessagesReceived());
    }


    private void publishSimulatingMessageDrop(String topic, String payload)
    {
        simulator.publishSimulatingMessageDrop(() -> {
            CompletableFuture<Mqtt5PublishResult> future = testPublisherClient.publish(Mqtt5Publish.builder()
                            .topic(topic)
                            .payload(payload.getBytes(UTF_8))
                            .qos(MqttQos.EXACTLY_ONCE)
                            .build());
            return future;
        });
    }


    private void startPublisherClient(String clientId)
    {
        this.testPublisherClient = new ConnectorFactory().newAsynchronousMQTTConnectorForPublisher("0.0.0.0", 1883, clientId, brokerServerMetrics).getClient();
    }


    private void startSubscriberClient(String topic, MqttQos qualityOfServiceLevel, String clientId)
    {
        testSubscriberClientWrapper = new ConnectorFactory().newAsynchronousMQTTConnectorForSubscriber("0.0.0.0", 1883, topic, qualityOfServiceLevel, clientId, brokerServerMetrics);
        this.testSubscriberClient = testSubscriberClientWrapper.getClient();
    }
}
