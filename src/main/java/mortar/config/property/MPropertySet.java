package mortar.config.property;

import java.util.UUID;

import mortar.lang.collection.GList;
import mortar.lang.collection.GMap;

public class MPropertySet implements PropertySet
{
	private GMap<String, Object> properties;

	public MPropertySet()
	{
		properties = new GMap<>();
	}

	@Override
	public PropertySet getSet(String key)
	{
		if(!properties.containsKey(key))
		{
			properties.put(key, new MPropertySet());
		}

		return (PropertySet) properties.get(key);
	}

	@Override
	public PropertySet putSet(String key, PropertySet set)
	{
		properties.put(key, set);
		return this;
	}

	@Override
	public Integer getInt(String key, Integer defaultValue)
	{
		if(!properties.containsKey(key))
		{
			return defaultValue;
		}

		return (Integer) properties.get(key);
	}

	@Override
	public Long getLong(String key, Long defaultValue)
	{
		if(!properties.containsKey(key))
		{
			return defaultValue;
		}

		return (Long) properties.get(key);
	}

	@Override
	public Double getDouble(String key, Double defaultValue)
	{
		if(!properties.containsKey(key))
		{
			return defaultValue;
		}

		return (Double) properties.get(key);
	}

	@Override
	public Float getFloat(String key, Float defaultValue)
	{
		if(!properties.containsKey(key))
		{
			return defaultValue;
		}

		return (Float) properties.get(key);
	}

	@Override
	public Short getShort(String key, Short defaultValue)
	{
		if(!properties.containsKey(key))
		{
			return defaultValue;
		}

		return (Short) properties.get(key);
	}

	@Override
	public Byte getByte(String key, Byte defaultValue)
	{
		if(!properties.containsKey(key))
		{
			return defaultValue;
		}

		return (Byte) properties.get(key);
	}

	@Override
	public String getString(String key, String defaultValue)
	{
		if(!properties.containsKey(key))
		{
			return defaultValue;
		}

		return (String) properties.get(key);
	}

	@Override
	public UUID getUUID(String key, UUID defaultValue)
	{
		if(!properties.containsKey(key))
		{
			return defaultValue;
		}

		return (UUID) properties.get(key);
	}

	@Override
	public PropertySet putInt(String key, Integer value)
	{
		properties.put(key, value);
		return this;
	}

	@Override
	public PropertySet putLong(String key, Long value)
	{
		properties.put(key, value);
		return this;
	}

	@Override
	public PropertySet putDouble(String key, Double value)
	{
		properties.put(key, value);
		return this;
	}

	@Override
	public PropertySet putFloat(String key, Float value)
	{
		properties.put(key, value);
		return this;
	}

	@Override
	public PropertySet putShort(String key, Short value)
	{
		properties.put(key, value);
		return this;
	}

	@Override
	public PropertySet putByte(String key, Byte value)
	{
		properties.put(key, value);
		return this;
	}

	@Override
	public PropertySet putString(String key, String value)
	{
		properties.put(key, value);
		return this;
	}

	@Override
	public PropertySet putUUID(String key, UUID value)
	{
		properties.put(key, value);
		return this;
	}

	@Override
	public GList<String> getKeys()
	{
		return properties.k();
	}

	@Override
	public boolean hasKey(String key)
	{
		return properties.containsKey(key);
	}

	@Override
	public PropertySet clear()
	{
		properties.clear();
		return this;
	}

	@Override
	public Object getRaw(String i)
	{
		return properties.get(i);
	}

	@Override
	public PropertySet putRaw(String i, Object o)
	{
		properties.put(i, o);
		return this;
	}

	@Override
	public PropertySet remove(String i)
	{
		properties.remove(i);
		return this;
	}
}
