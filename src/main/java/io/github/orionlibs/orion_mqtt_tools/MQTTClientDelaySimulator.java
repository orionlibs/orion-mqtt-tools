package io.github.orionlibs.orion_mqtt_tools;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * simulation methods for several scenarios that can help MQTT client and broker developers
 * get client behaviours that will help them determine how their code handles these
 * situations. These methods are overloaded in order to take either a Runnable (that does not return a value)
 * or a Supplier<?> that returns a value when called. This was created in order to test
 * MQTT client methods that (depending on their implementation) return a value or not
 */
public class MQTTClientDelaySimulator
{
    private final Random random;
    private final MessageResiliencySimulationConfiguration resiliencyConfig;


    public MQTTClientDelaySimulator(MessageResiliencySimulationConfiguration resiliencyConfig)
    {
        this.resiliencyConfig = resiliencyConfig;
        this.random = new Random();
    }


    public Object publishSimulatingMessageDrop(Supplier<?> publishAction)
    {
        //simulate message drop
        if(resiliencyConfig.getDropProbability() > 0 && random.nextDouble() < resiliencyConfig.getDropProbability())
        {
            System.out.println("simulating message drop, message not published");
            return null; //simulate message drop by not calling the action
        }
        else
        {
            return publishAction.get();
        }
    }


    public void publishSimulatingMessageDrop(Runnable publishAction)
    {
        //simulate message drop
        if(resiliencyConfig.getDropProbability() > 0 && random.nextDouble() < resiliencyConfig.getDropProbability())
        {
            System.out.println("simulating message drop, message not published");
        }
        else
        {
            publishAction.run();
        }
    }


    public Object publishSimulatingDuplicateMessage(Supplier<?> publishAction)
    {
        Object result = null;
        //simulate message duplication
        if(resiliencyConfig.getDuplicationProbability() > 0 && random.nextDouble() < resiliencyConfig.getDuplicationProbability())
        {
            System.out.println("Simulating message duplication.");
            result = publishAction.get(); // Publish message once
        }
        result = publishAction.get();
        return result;
    }


    public void publishSimulatingDuplicateMessage(Runnable publishAction)
    {
        //simulate message duplication
        if(resiliencyConfig.getDuplicationProbability() > 0 && random.nextDouble() < resiliencyConfig.getDuplicationProbability())
        {
            System.out.println("Simulating message duplication.");
            publishAction.run(); // Publish message once
        }
        publishAction.run();
    }


    public Object publishSimulatingDelay(Supplier<?> publishAction)
    {
        try
        {
            if(resiliencyConfig.getDelayProbability() > 0 && random.nextDouble() < resiliencyConfig.getDelayProbability())
            {
                long delay = resiliencyConfig.getMaxDelayMillis();
                System.out.println("Simulating delay: " + delay + "ms");
                TimeUnit.MILLISECONDS.sleep(delay);
            }
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
            System.err.println("Message simulation interrupted.");
            return null;
        }
        return publishAction.get();
    }


    public void publishSimulatingDelay(Runnable publishAction)
    {
        try
        {
            if(resiliencyConfig.getDelayProbability() > 0 && random.nextDouble() < resiliencyConfig.getDelayProbability())
            {
                long delay = resiliencyConfig.getMaxDelayMillis();
                System.out.println("Simulating delay: " + delay + "ms");
                TimeUnit.MILLISECONDS.sleep(delay);
            }
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
            System.err.println("Message simulation interrupted.");
        }
        publishAction.run();
    }
}
