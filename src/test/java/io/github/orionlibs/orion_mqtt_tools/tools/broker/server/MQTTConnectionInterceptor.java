package io.github.orionlibs.orion_mqtt_tools.tools.broker.server;

import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.interceptor.connect.ConnectInboundInterceptor;
import com.hivemq.extension.sdk.api.interceptor.connect.parameter.ConnectInboundInput;
import com.hivemq.extension.sdk.api.interceptor.connect.parameter.ConnectInboundOutput;
import io.github.orionlibs.orion_mqtt_tools.ConnectionPolicies;
import io.github.orionlibs.orion_mqtt_tools.MQTTBrokerServerMetrics;
import io.github.orionlibs.orion_mqtt_tools.MQTTClientType;
import io.github.orionlibs.orion_mqtt_tools.MQTTUserProperties;

public class MQTTConnectionInterceptor implements ConnectInboundInterceptor
{
    private ConnectionPolicies connectionPolicies;
    private MQTTBrokerServerMetrics brokerServerMetrics;


    public MQTTConnectionInterceptor(ConnectionPolicies connectionPolicies, MQTTBrokerServerMetrics brokerServerMetrics)
    {
        this.connectionPolicies = connectionPolicies;
        this.brokerServerMetrics = brokerServerMetrics;
    }


    @Override
    public void onConnect(@NotNull ConnectInboundInput connectInboundInput, @NotNull ConnectInboundOutput connectInboundOutput)
    {
        System.out.println("---new connection request: " + connectInboundInput.getConnectPacket().getClientId());
        System.out.println("---new connection request: " + connectInboundInput.getConnectPacket().getUserProperties().getFirst(MQTTUserProperties.CLIENT_TYPE).get());
        if(MQTTClientType.PUBLISHER.get().equals(connectInboundInput.getConnectPacket().getUserProperties().getFirst(MQTTUserProperties.CLIENT_TYPE).get()))
        {
            if(connectionPolicies.allowNewPublisherConnection(brokerServerMetrics.getCurrentNumberOfPublishersConnections().get()))
            {
                brokerServerMetrics.incrementNumberOfPublishersConnections();
            }
            else
            {
                //throw new Exception("exceeded the maximum number of publishers allowed");
            }
        }
        else if(MQTTClientType.SUBSCRIBER.get().equals(connectInboundInput.getConnectPacket().getUserProperties().getFirst(MQTTUserProperties.CLIENT_TYPE).get()))
        {
            if(connectionPolicies.allowNewSubscriberConnection(brokerServerMetrics.getCurrentNumberOfSubscribersConnections().get()))
            {
                brokerServerMetrics.incrementNumberOfSubscribersConnections();
            }
            else
            {
                //throw new Exception("exceeded the maximum number of subscribers allowed");
            }
        }
        else
        {
            if(connectionPolicies.allowNewConnection(brokerServerMetrics.getCurrentNumberOfAllConnections().get()))
            {
                brokerServerMetrics.incrementNumberOfAllConnections();
            }
            else
            {
                //throw new Exception("exceeded the maximum number of subscribers allowed");
            }
        }
    }
}
