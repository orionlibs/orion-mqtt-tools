package io.github.orionlibs.orion_mqtt_tools.broker.server;

import com.hivemq.extension.sdk.api.auth.Authenticator;
import com.hivemq.extension.sdk.api.auth.SimpleAuthenticator;
import com.hivemq.extension.sdk.api.auth.parameter.AuthenticatorProviderInput;
import com.hivemq.extension.sdk.api.auth.parameter.SimpleAuthInput;
import com.hivemq.extension.sdk.api.auth.parameter.SimpleAuthOutput;
import com.hivemq.extension.sdk.api.services.auth.provider.AuthenticatorProvider;
import java.nio.ByteBuffer;
import java.util.Optional;

public class MQTTAuthenticatorProvider implements AuthenticatorProvider
{
    @Override
    public Authenticator getAuthenticator(AuthenticatorProviderInput authenticatorProviderInput)
    {
        return new SimpleAuthenticator()
        {
            @Override
            public void onConnect(SimpleAuthInput input, SimpleAuthOutput output)
            {
                Optional<String> username = input.getConnectPacket().getUserName();
                Optional<ByteBuffer> password = input.getConnectPacket().getPassword();
                if(!username.isPresent() || !password.isPresent())
                {
                    output.failAuthentication();
                    return;
                }
                ByteBuffer buffer = password.get();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                String passwordString = new String(bytes);
                if(isValidCredentials(username.get(), passwordString))
                {
                    output.authenticateSuccessfully();
                }
                else
                {
                    output.failAuthentication();
                }
            }
        };
    }


    private boolean isValidCredentials(String username, String password)
    {
        // Replace this with your actual authentication logic
        // For example, check against a database, LDAP, or other authentication service
        return "admin".equals(username) && "password".equals(password);
    }
}
