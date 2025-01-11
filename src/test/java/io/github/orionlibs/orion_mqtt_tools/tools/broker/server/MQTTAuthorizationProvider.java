package io.github.orionlibs.orion_mqtt_tools.tools.broker.server;

import com.hivemq.extension.sdk.api.auth.Authorizer;
import com.hivemq.extension.sdk.api.auth.parameter.AuthorizerProviderInput;
import com.hivemq.extension.sdk.api.services.auth.provider.AuthorizerProvider;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class MQTTAuthorizationProvider implements AuthorizerProvider
{
    private final static Logger log;
    //private MQTTSubscriberAuthorizationProvider subscriberAuthorizer = new MQTTSubscriberAuthorizationProvider();
    private MQTTPublisherAuthorizationProvider publisherAuthorizer = new MQTTPublisherAuthorizationProvider();

    static
    {
        log = Logger.getLogger(MQTTAuthorizationProvider.class.getName());
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
        return publisherAuthorizer;
    }
}
