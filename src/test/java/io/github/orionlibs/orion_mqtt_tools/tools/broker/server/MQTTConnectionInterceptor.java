package io.github.orionlibs.orion_mqtt_tools.tools.broker.server;

import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.interceptor.connect.ConnectInboundInterceptor;
import com.hivemq.extension.sdk.api.interceptor.connect.parameter.ConnectInboundInput;
import com.hivemq.extension.sdk.api.interceptor.connect.parameter.ConnectInboundOutput;
import io.github.orionlibs.orion_mqtt_tools.ConnectionPolicies;

public class MQTTConnectionInterceptor implements ConnectInboundInterceptor
{
    private ConnectionPolicies connectionPolicies;


    public MQTTConnectionInterceptor(ConnectionPolicies connectionPolicies)
    {
        this.connectionPolicies = connectionPolicies;
    }


    @Override
    public void onConnect(@NotNull ConnectInboundInput connectInboundInput, @NotNull ConnectInboundOutput connectInboundOutput)
    {
        System.out.println("---" + connectInboundInput.getConnectPacket().getClientId());
        /*connectInboundInput.getConnectPacket()
                        .getUserProperties()
                        .getFirst("clientType")
                        .ifPresent(connectionPolicies::allowNewPublisherConnection);*/
    }
}
