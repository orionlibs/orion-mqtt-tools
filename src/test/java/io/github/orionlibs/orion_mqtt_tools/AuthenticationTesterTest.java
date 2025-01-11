package io.github.orionlibs.orion_mqtt_tools;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
        boolean result = authenticationTester.testCredentials("0.0.0.0", 1883, "admin", "password");
        Utils.nonblockingDelay(3);
        assertTrue(result);
    }
}
