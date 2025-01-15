package io.github.orionlibs.orion_mqtt_tools;

import io.github.orionlibs.orion_enumeration.OrionEnumeration;

public enum MQTTClientType implements OrionEnumeration
{
    PUBLISHER("PUBLISHER"),
    UNSUBSCRIBER("UNSUBSCRIBER"),
    SUBSCRIBER("SUBSCRIBER");
    private String name;


    private MQTTClientType(String name)
    {
        setName(name);
    }


    @Override
    public String get()
    {
        return getName();
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    @Override
    public boolean is(OrionEnumeration other)
    {
        return other instanceof MQTTClientType && this == other;
    }


    @Override
    public boolean isNot(OrionEnumeration other)
    {
        return other instanceof MQTTClientType && this != other;
    }


    public static boolean valueExists(String other)
    {
        MQTTClientType[] values = values();
        for(MQTTClientType value : values)
        {
            if(value.get().equals(other))
            {
                return true;
            }
        }
        return false;
    }


    public static MQTTClientType getEnumForValue(String other)
    {
        MQTTClientType[] values = values();
        for(MQTTClientType value : values)
        {
            if(value.get().equals(other))
            {
                return value;
            }
        }
        return null;
    }
}
