package io.github.orionlibs.orion_mqtt_tools.tools.broker.server;

import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.interceptor.connect.ConnectInboundInterceptor;
import com.hivemq.extension.sdk.api.interceptor.connect.parameter.ConnectInboundInput;
import com.hivemq.extension.sdk.api.interceptor.connect.parameter.ConnectInboundOutput;
import io.github.orionlibs.orion_mqtt_tools.ConnectionPolicies;
import io.github.orionlibs.orion_mqtt_tools.MQTTBrokerServerMetrics;
import io.github.orionlibs.orion_mqtt_tools.MQTTClientType;
import io.github.orionlibs.orion_mqtt_tools.MQTTUserProperties;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class MQTTConnectionInterceptor implements ConnectInboundInterceptor
{
    private final static Logger log;
    private ConnectionPolicies connectionPolicies;
    private MQTTBrokerServerMetrics brokerServerMetrics;

    static
    {
        log = Logger.getLogger(MQTTConnectionInterceptor.class.getName());
    }

    public static void addLogHandler(Handler handler)
    {
        log.addHandler(handler);
    }


    public static void removeLogHandler(Handler handler)
    {
        log.removeHandler(handler);
    }


    public MQTTConnectionInterceptor(ConnectionPolicies connectionPolicies, MQTTBrokerServerMetrics brokerServerMetrics)
    {
        this.connectionPolicies = connectionPolicies;
        this.brokerServerMetrics = brokerServerMetrics;
    }


    @Override
    public void onConnect(@NotNull ConnectInboundInput connectInboundInput, @NotNull ConnectInboundOutput connectInboundOutput)
    {
        System.out.println("---new connection request: " + connectInboundInput.getConnectPacket().getUserProperties().getFirst(MQTTUserProperties.CLIENT_TYPE).get());
        if(MQTTClientType.PUBLISHER.get().equals(connectInboundInput.getConnectPacket().getUserProperties().getFirst(MQTTUserProperties.CLIENT_TYPE).get()))
        {
            if(!connectionPolicies.allowNewPublisherConnection(brokerServerMetrics.getCurrentNumberOfPublishersConnections().get()))
            {
                log.severe("exceeded the maximum number of publishers allowed");
                throw new IllegalStateException("exceeded the maximum number of publishers allowed");
            }
        }
        else if(MQTTClientType.SUBSCRIBER.get().equals(connectInboundInput.getConnectPacket().getUserProperties().getFirst(MQTTUserProperties.CLIENT_TYPE).get()))
        {
            if(!connectionPolicies.allowNewSubscriberConnection(brokerServerMetrics.getCurrentNumberOfSubscribersConnections().get()))
            {
                log.severe("exceeded the maximum number of subscribers allowed");
                throw new IllegalStateException("exceeded the maximum number of subscribers allowed");
            }
        }
        else
        {
            if(!connectionPolicies.allowNewConnection(brokerServerMetrics.getCurrentNumberOfAllConnections().get()))
            {
                log.severe("exceeded the maximum number of connections allowed");
                throw new IllegalStateException("exceeded the maximum number of connections allowed");
            }
        }
    }
}
