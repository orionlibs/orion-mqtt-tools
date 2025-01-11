package io.github.orionlibs.orion_mqtt_tools;

public class MessageResiliencyConfiguration
{
    private final double delayProbability;
    private final long maxDelayMillis;
    private final double dropProbability;
    private final double duplicationProbability;


    public MessageResiliencyConfiguration(double delayProbability, long maxDelayMillis, double dropProbability, double duplicationProbability)
    {
        this.delayProbability = delayProbability;
        this.maxDelayMillis = maxDelayMillis;
        this.dropProbability = dropProbability;
        this.duplicationProbability = duplicationProbability;
    }


    public double getDelayProbability()
    {
        return delayProbability;
    }


    public long getMaxDelayMillis()
    {
        return maxDelayMillis;
    }


    public double getDropProbability()
    {
        return dropProbability;
    }


    public double getDuplicationProbability()
    {
        return duplicationProbability;
    }
}
