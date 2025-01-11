package io.github.orionlibs.orion_mqtt_tools;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.orionlibs.orion_mqtt_tools.broker.client.HiveMQClientAdapter;
import io.github.orionlibs.orion_mqtt_tools.broker.server.MQTTBrokerServer;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
//@Execution(ExecutionMode.CONCURRENT)
public class AuthenticationTesterTest extends ATest
{
    private MQTTBrokerServer brokerServer;
    private String clientID = "testClientId";
    private AuthenticationTester authenticationTester;


    @BeforeEach
    void setUp() throws ExecutionException, InterruptedException, URISyntaxException
    {
        brokerServer = new MQTTBrokerServer();
        brokerServer.startBroker(true);
        Utils.nonblockingDelay(3);
        authenticationTester = new AuthenticationTester(new HiveMQClientAdapter());
    }


    @AfterEach
    void teardown()
    {
        brokerServer.stopBroker();
    }


    @Test
    void testClientAuthentication()
    {
        try
        {
            authenticationTester.testCredentialsWithDelay("0.0.0.0", 1883, clientID, "admin", "password", 2);
        }
        catch(Exception e)
        {
            assertFalse(false);
        }
        assertThrows(
                        Exception.class,
                        () -> authenticationTester.testCredentialsWithDelay("0.0.0.0", 1883, clientID, "admin", "wrongpassword", 2)
        );
        assertThrows(
                        Exception.class,
                        () -> authenticationTester.testCredentialsWithDelay("0.0.0.0", 1883, clientID, "wronguser", "password", 2)
        );
        assertThrows(
                        Exception.class,
                        () -> authenticationTester.testCredentialsWithDelay("0.0.0.0", 1883, clientID, "wronguser", "wrongpassword", 2)
        );
    }
}
