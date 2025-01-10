package io.github.orionlibs.orion_mqtt_tools;

public class Utils
{
    public static void nonblockingDelay(int numberOfSeconds)
    {
        long durationNanos = numberOfSeconds * 1_000_000_000L;
        long startTime = System.nanoTime();
        while(true)
        {
            long currentTime = System.nanoTime();
            long elapsedTime = currentTime - startTime;
            if(elapsedTime >= durationNanos)
            {
                break;
            }
            for(int i = 0; i < 10; i++)
            {
                Math.sin(i);
            }
        }
    }
}
