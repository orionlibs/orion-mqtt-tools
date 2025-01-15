package io.github.orionlibs.orion_mqtt_tools.tools.broker.server;

import com.hivemq.extension.sdk.api.interceptor.connect.ConnectInboundInterceptor;
import com.hivemq.extension.sdk.api.interceptor.connect.ConnectInboundInterceptorProvider;
import com.hivemq.extension.sdk.api.interceptor.connect.parameter.ConnectInboundProviderInput;
import io.github.orionlibs.orion_mqtt_tools.ConnectionPolicies;
import io.github.orionlibs.orion_mqtt_tools.MQTTBrokerConfiguration;
import io.github.orionlibs.orion_mqtt_tools.MQTTBrokerServerMetrics;

public class MQTTConnectInterceptorProvider implements ConnectInboundInterceptorProvider
{
    private MQTTBrokerConfiguration brokerConfiguration;
    private MQTTBrokerServerMetrics brokerServerMetrics;


    public MQTTConnectInterceptorProvider(MQTTBrokerConfiguration brokerConfiguration, MQTTBrokerServerMetrics brokerServerMetrics)
    {
        this.brokerConfiguration = brokerConfiguration;
        this.brokerServerMetrics = brokerServerMetrics;
    }


    @Override
    public ConnectInboundInterceptor getConnectInboundInterceptor(ConnectInboundProviderInput connectInboundProviderInput)
    {
        return new MQTTConnectionInterceptor(new ConnectionPolicies(brokerConfiguration), brokerServerMetrics);
    }
}
