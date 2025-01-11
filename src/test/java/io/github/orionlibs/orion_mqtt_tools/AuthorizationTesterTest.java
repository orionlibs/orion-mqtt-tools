package io.github.orionlibs.orion_mqtt_tools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.orionlibs.orion_mqtt_tools.tools.broker.client.HiveMQClientAdapter;
import io.github.orionlibs.orion_mqtt_tools.tools.broker.server.MQTTAuthorizationProvider;
import io.github.orionlibs.orion_mqtt_tools.tools.broker.server.MQTTBrokerServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
//@Execution(ExecutionMode.CONCURRENT)
public class AuthorizationTesterTest extends ATest
{
    private ListLogHandler listLogHandler;
    private MQTTBrokerServer brokerServer;
    private String clientID = "testClientId";
    private AuthorizationTester authorizationTester;
    private HiveMQClientAdapter clientAdapter;


    @BeforeEach
    void setUp() throws Exception
    {
        listLogHandler = new ListLogHandler();
        MQTTAuthorizationProvider.addLogHandler(listLogHandler);
        brokerServer = new MQTTBrokerServer();
        brokerServer.startBroker(true, true);
        Utils.nonblockingDelay(3);
        clientAdapter = new HiveMQClientAdapter("0.0.0.0", 1883, clientID, "admin", "password");
        authorizationTester = new AuthorizationTester(clientAdapter);
    }


    @AfterEach
    void teardown()
    {
        MQTTAuthorizationProvider.removeLogHandler(listLogHandler);
        clientAdapter.disconnect();
        brokerServer.stopBroker();
    }


    @Test
    void testPublishClientAuthorization() throws Exception
    {
        Utils.nonblockingDelay(2);
        authorizationTester.testPublishAuthorizationWithDelay("admin/topic", "somePayload1".getBytes(), 2);
        assertEquals(0, listLogHandler.getLogRecords().size());
        authorizationTester.testPublishAuthorizationWithDelay("forbidden/topic", "somePayload1".getBytes(), 2);
        assertEquals(1, listLogHandler.getLogRecords().size());
        assertTrue(listLogHandler.getLogRecords()
                        .stream()
                        .anyMatch(record -> record.getMessage().contains("forbidden")));
        MQTTCMessageAdapter messageAdapter = new MQTTCMessageAdapter();
        authorizationTester.testSubscribeAuthorizationWithDelay("admin/topic", messageAdapter, 2);
        assertEquals(1, listLogHandler.getLogRecords().size());
        authorizationTester.testSubscribeAuthorizationWithDelay("$shared/topic", messageAdapter, 2);
        assertEquals(2, listLogHandler.getLogRecords().size());
        assertTrue(listLogHandler.getLogRecords()
                        .stream()
                        .anyMatch(record -> record.getMessage().contains("$shared")));
    }
}
