package io.github.orionlibs.orion_mqtt_tools.tools.broker.client;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.hivemq.client.internal.mqtt.datatypes.MqttUserPropertyImpl;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.datatypes.Mqtt5UserProperty;
import com.hivemq.client.mqtt.mqtt5.message.connect.Mqtt5Connect;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import com.hivemq.extension.sdk.api.services.Services;
import com.hivemq.extension.sdk.api.services.general.IterationCallback;
import com.hivemq.extension.sdk.api.services.general.IterationContext;
import com.hivemq.extension.sdk.api.services.subscription.SubscriberWithFilterResult;
import io.github.orionlibs.orion_mqtt_tools.MQTTClientType;
import io.github.orionlibs.orion_mqtt_tools.MQTTUserProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MQTTAsynchronousPublisherClient
{
    private Mqtt5AsyncClient client;


    public MQTTAsynchronousPublisherClient(String brokerUrl, int port, String clientId)
    {
        this.client = Mqtt5Client.builder()
                        .identifier(clientId)
                        .serverHost(brokerUrl)
                        .serverPort(port)
                        .buildAsync();
        Mqtt5Connect connectMessage = Mqtt5Connect.builder()
                        .userProperties()
                        .add(MQTTUserProperties.CLIENT_TYPE, MQTTClientType.PUBLISHER.get())
                        .applyUserProperties()
                        .build();
        client.connect(connectMessage)
                        .exceptionally(throwable -> {
                            System.out.println("Something went wrong publisher: " + throwable.getMessage());
                            return null;
                        });
    }


    public MQTTAsynchronousPublisherClient(String brokerUrl, int port, String topic, String payload, String clientId)
    {
        this.client = Mqtt5Client.builder()
                        .identifier(clientId)
                        .serverHost(brokerUrl)
                        .serverPort(port)
                        .buildAsync();
        Mqtt5Connect connectMessage = Mqtt5Connect.builder()
                        .userProperties()
                        .add(MQTTUserProperties.CLIENT_TYPE, MQTTClientType.PUBLISHER.get())
                        .applyUserProperties()
                        .build();
        client.connect(connectMessage)
                        .thenCompose(connAck -> {
                            System.out.println("Successfully connected publisher!");
                            List<String> subscriberIDsForTopic = new ArrayList<>();
                            IterationCallback<SubscriberWithFilterResult> subscribersForTopic = new IterationCallback()
                            {
                                @Override
                                public void iterate(IterationContext iterationContext, Object o)
                                {
                                    SubscriberWithFilterResult result = (SubscriberWithFilterResult)o;
                                    subscriberIDsForTopic.add(result.getClientId());
                                }
                            };
                            CompletableFuture<Void> searchResults = Services.subscriptionStore().iterateAllSubscribersWithTopicFilter(topic, subscribersForTopic);
                            List<Mqtt5UserProperty> subscribers = new ArrayList<>();
                            searchResults.thenRun(() -> {
                                subscriberIDsForTopic.forEach(subscriberID -> subscribers.add(MqttUserPropertyImpl.of("subscriberId", subscriberID)));
                            });
                            final Mqtt5Publish publish = Mqtt5Publish.builder()
                                            .topic(topic)
                                            .payload(payload.getBytes(UTF_8))
                                            .qos(MqttQos.EXACTLY_ONCE)
                                            .build();
                            final var future = client.publish(publish);
                            return CompletableFuture.allOf(future);
                        }).thenCompose(unused -> {
                            return client.disconnect();
                        }).exceptionally(throwable -> {
                            System.out.println("Something went wrong publisher: " + throwable.getMessage());
                            return null;
                        });
    }


    public Mqtt5AsyncClient getClient()
    {
        return client;
    }
}
