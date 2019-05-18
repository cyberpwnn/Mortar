package mortar.config.property;

import java.util.UUID;

import mortar.lang.collection.GList;

public interface PropertySet
{
	public static byte getType(Object o)
	{
		if(o == null)
		{
			return -1;
		}

		if(o instanceof PropertySet)
		{
			return 0;
		}

		if(o instanceof Integer || o.getClass().equals(int.class))
		{
			return 1;
		}

		if(o instanceof Long || o.getClass().equals(long.class))
		{
			return 2;
		}

		if(o instanceof Double || o.getClass().equals(double.class))
		{
			return 3;
		}

		if(o instanceof Float || o.getClass().equals(float.class))
		{
			return 4;
		}

		if(o instanceof Short || o.getClass().equals(short.class))
		{
			return 5;
		}

		if(o instanceof Byte || o.getClass().equals(byte.class))
		{
			return 6;
		}

		if(o instanceof String)
		{
			return 7;
		}

		if(o instanceof UUID)
		{
			return 8;
		}

		return -1;
	}

	public static Class<?> getType(byte type)
	{
		switch(type)
		{
			case 0:
				return PropertySet.class;
			case 1:
				return Integer.class;
			case 2:
				return Long.class;
			case 3:
				return Double.class;
			case 4:
				return Float.class;
			case 5:
				return Short.class;
			case 6:
				return Byte.class;
			case 7:
				return String.class;
			case 8:
				return UUID.class;
		}

		return null;
	}

	public PropertySet getSet(String key);

	public PropertySet putSet(String key, PropertySet set);

	public Integer getInt(String key, Integer defaultValue);

	public Long getLong(String key, Long defaultValue);

	public Double getDouble(String key, Double defaultValue);

	public Float getFloat(String key, Float defaultValue);

	public Short getShort(String key, Short defaultValue);

	public Byte getByte(String key, Byte defaultValue);

	public String getString(String key, String defaultValue);

	public UUID getUUID(String key, UUID defaultValue);

	public PropertySet putInt(String key, Integer value);

	public PropertySet putLong(String key, Long value);

	public PropertySet putDouble(String key, Double value);

	public PropertySet putFloat(String key, Float value);

	public PropertySet putShort(String key, Short value);

	public PropertySet putByte(String key, Byte value);

	public PropertySet putString(String key, String value);

	public PropertySet putUUID(String key, UUID value);

	public GList<String> getKeys();

	public boolean hasKey(String key);

	public PropertySet clear();

	public Object getRaw(String i);

	public PropertySet putRaw(String i, Object o);

	public PropertySet remove(String i);
}
