package io.github.orionlibs.orion_mqtt_tools.tools.broker.server;

import com.hivemq.extension.sdk.api.auth.Authorizer;
import com.hivemq.extension.sdk.api.auth.SubscriptionAuthorizer;
import com.hivemq.extension.sdk.api.auth.parameter.AuthorizerProviderInput;
import com.hivemq.extension.sdk.api.auth.parameter.SubscriptionAuthorizerInput;
import com.hivemq.extension.sdk.api.auth.parameter.SubscriptionAuthorizerOutput;
import com.hivemq.extension.sdk.api.packets.disconnect.DisconnectReasonCode;
import com.hivemq.extension.sdk.api.packets.general.UserProperties;
import com.hivemq.extension.sdk.api.services.auth.provider.AuthorizerProvider;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class MQTTSubscriberAuthorizationProvider implements AuthorizerProvider, SubscriptionAuthorizer
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
    public Authorizer getAuthorizer(AuthorizerProviderInput authorizerProviderInput)
    {
        return this;
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
            log.severe("$shared");
            subscriptionAuthorizerOutput.failAuthorization();
            return;
        }
        final UserProperties userProperties = subscriptionAuthorizerInput.getUserProperties();
        if(userProperties.getFirst("notallowed").isPresent())
        {
            log.severe("notallowed");
            subscriptionAuthorizerOutput.disconnectClient(DisconnectReasonCode.ADMINISTRATIVE_ACTION, "User property not allowed");
            return;
        }
        subscriptionAuthorizerOutput.nextExtensionOrDefault();
    }
}
