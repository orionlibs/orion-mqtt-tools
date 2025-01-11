package io.github.orionlibs.orion_mqtt_tools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.orionlibs.orion_mqtt_tools.tools.broker.client.HiveMQClientAdapter;
import io.github.orionlibs.orion_mqtt_tools.tools.broker.server.MQTTAuthenticatorProvider;
import io.github.orionlibs.orion_mqtt_tools.tools.broker.server.MQTTBrokerServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
//@Execution(ExecutionMode.CONCURRENT)
public class AuthenticationTesterTest extends ATest
{
    private ListLogHandler listLogHandler;
    private MQTTBrokerServer brokerServer;
    private String clientID = "testClientId";
    private AuthenticationTester authenticationTester;


    @BeforeEach
    void setUp() throws Exception
    {
        listLogHandler = new ListLogHandler();
        MQTTAuthenticatorProvider.addLogHandler(listLogHandler);
        brokerServer = new MQTTBrokerServer();
        brokerServer.startBroker(true, false);
        Utils.nonblockingDelay(3);
        authenticationTester = new AuthenticationTester(new HiveMQClientAdapter());
    }


    @AfterEach
    void teardown()
    {
        MQTTAuthenticatorProvider.removeLogHandler(listLogHandler);
        brokerServer.stopBroker();
    }


    @Test
    void testClientAuthentication() throws Exception
    {
        authenticationTester.testCredentialsWithDelay("0.0.0.0", 1883, clientID, "admin", "password", 2);
        assertEquals(0, listLogHandler.getLogRecords().size());
        authenticationTester.testCredentialsWithDelay("0.0.0.0", 1883, clientID, "admin", "wrongpassword", 2);
        assertEquals(1, listLogHandler.getLogRecords().size());
        assertTrue(listLogHandler.getLogRecords()
                        .stream()
                        .anyMatch(record -> record.getMessage().contains("NOT_AUTHORIZED_0")));
        authenticationTester.testCredentialsWithDelay("0.0.0.0", 1883, clientID, "wronguser", "password", 2);
        assertEquals(2, listLogHandler.getLogRecords().size());
        assertTrue(listLogHandler.getLogRecords()
                        .stream()
                        .anyMatch(record -> record.getMessage().contains("NOT_AUTHORIZED_1")));
        authenticationTester.testCredentialsWithDelay("0.0.0.0", 1883, clientID, "wronguser", "wrongpassword", 2);
        assertEquals(3, listLogHandler.getLogRecords().size());
        assertTrue(listLogHandler.getLogRecords()
                        .stream()
                        .anyMatch(record -> record.getMessage().contains("NOT_AUTHORIZED_2")));
    }
}
