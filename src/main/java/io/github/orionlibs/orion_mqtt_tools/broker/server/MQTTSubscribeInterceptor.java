package io.github.orionlibs.orion_mqtt_tools.broker.server;

import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.interceptor.subscribe.SubscribeInboundInterceptor;
import com.hivemq.extension.sdk.api.interceptor.subscribe.parameter.SubscribeInboundInput;
import com.hivemq.extension.sdk.api.interceptor.subscribe.parameter.SubscribeInboundOutput;

public class MQTTSubscribeInterceptor implements SubscribeInboundInterceptor
{
    @Override
    public void onInboundSubscribe(@NotNull SubscribeInboundInput subscribeInboundInput, @NotNull SubscribeInboundOutput subscribeInboundOutput)
    {
        String clientId = subscribeInboundInput.getClientInformation().getClientId();
        subscribeInboundInput.getSubscribePacket()
                        .getSubscriptions()
                        .forEach(subscription -> {
                            System.out.println("subscribed");
                        });
        // Optional: Reject subscriptions
        // subscribeInboundOutput.preventSubscription();
    }
}
