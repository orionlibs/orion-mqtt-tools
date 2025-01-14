package io.github.orionlibs.orion_mqtt_tools.tools.broker.server;

import com.hivemq.extension.sdk.api.interceptor.connect.ConnectInboundInterceptor;
import com.hivemq.extension.sdk.api.interceptor.connect.ConnectInboundInterceptorProvider;
import com.hivemq.extension.sdk.api.interceptor.connect.parameter.ConnectInboundProviderInput;

public class MQTTConnectInterceptorProvider implements ConnectInboundInterceptorProvider
{
    @Override
    public ConnectInboundInterceptor getConnectInboundInterceptor(ConnectInboundProviderInput connectInboundProviderInput)
    {
        return null;
        //return new MQTTConnectionInterceptor();
    }
}
