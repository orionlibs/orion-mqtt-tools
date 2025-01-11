package io.github.orionlibs.orion_mqtt_tools;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MQTTCMessageAdapter implements MQTTClientAdapter.MqttMessageHandler
{
    @Override
    public void handleMessage(String topic, byte[] payload)
    {
        System.out.println("new message: " + new String(payload, UTF_8));
    }
}
