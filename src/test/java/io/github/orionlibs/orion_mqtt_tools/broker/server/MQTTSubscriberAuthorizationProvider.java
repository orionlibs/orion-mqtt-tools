package io.github.orionlibs.orion_mqtt_tools.broker.server;

import com.hivemq.extension.sdk.api.auth.SubscriptionAuthorizer;
import com.hivemq.extension.sdk.api.auth.parameter.SubscriptionAuthorizerInput;
import com.hivemq.extension.sdk.api.auth.parameter.SubscriptionAuthorizerOutput;
import com.hivemq.extension.sdk.api.packets.disconnect.DisconnectReasonCode;
import com.hivemq.extension.sdk.api.packets.general.UserProperties;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class MQTTSubscriberAuthorizationProvider implements SubscriptionAuthorizer
{
    private final static Logger log;

    static
    {
        log = Logger.getLogger(MQTTSubscriberAuthorizationProvider.class.getName());
    }

    public static void addLogHandler(Handler handler)
    {
        log.addHandler(handler);
    }


    public static void removeLogHandler(Handler handler)
    {
        log.removeHandler(handler);
    }


    @Override
    public void authorizeSubscribe(SubscriptionAuthorizerInput subscriptionAuthorizerInput, SubscriptionAuthorizerOutput subscriptionAuthorizerOutput)
    {
        //allow every Topic Filter starting with "admin"
        if(subscriptionAuthorizerInput.getSubscription().getTopicFilter().startsWith("admin"))
        {
            subscriptionAuthorizerOutput.authorizeSuccessfully();
            return;
        }
        //disallow a shared subscription
        if(subscriptionAuthorizerInput.getSubscription().getTopicFilter().startsWith("$shared"))
        {
            subscriptionAuthorizerOutput.failAuthorization();
            return;
        }
        final UserProperties userProperties = subscriptionAuthorizerInput.getUserProperties();
        if(userProperties.getFirst("notallowed").isPresent())
        {
            subscriptionAuthorizerOutput.disconnectClient(DisconnectReasonCode.ADMINISTRATIVE_ACTION, "User property not allowed");
            return;
        }
        subscriptionAuthorizerOutput.nextExtensionOrDefault();
    }
}
