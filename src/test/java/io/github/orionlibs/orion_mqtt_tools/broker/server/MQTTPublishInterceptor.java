package io.github.orionlibs.orion_mqtt_tools.broker.server;

import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.interceptor.publish.PublishInboundInterceptor;
import com.hivemq.extension.sdk.api.interceptor.publish.parameter.PublishInboundInput;
import com.hivemq.extension.sdk.api.interceptor.publish.parameter.PublishInboundOutput;
import com.hivemq.extension.sdk.api.packets.general.Qos;

public class MQTTPublishInterceptor implements PublishInboundInterceptor
{
    @Override
    public void onInboundPublish(@NotNull PublishInboundInput publishInboundInput, @NotNull PublishInboundOutput publishInboundOutput)
    {
        String publisherId = publishInboundInput.getClientInformation().getClientId();
        String topic = publishInboundInput.getPublishPacket().getTopic();
        if(publishInboundOutput.getPublishPacket().getQos() == Qos.AT_LEAST_ONCE
                        || publishInboundOutput.getPublishPacket().getQos() == Qos.EXACTLY_ONCE)
        {
            System.out.println("published");
        }
        // Optional: Modify the publish message
        // publishInboundOutput.getPublishPacket().setPayload(...);
    }
}
