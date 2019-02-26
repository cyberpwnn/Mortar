package mortar.bukkit.command;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import mortar.lang.collection.GList;

public abstract class MortarPermission
{
	private MortarPermission parent;

	public MortarPermission()
	{
		for(Field i : getClass().getDeclaredFields())
		{
			if(i.isAnnotationPresent(Permission.class))
			{
				try
				{
					MortarPermission px = (MortarPermission) i.getType().getConstructor().newInstance();
					px.setParent(this);
					i.set(Modifier.isStatic(i.getModifiers()) ? null : this, px);
				}

				catch(IllegalArgumentException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException | SecurityException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public GList<MortarPermission> getChildren()
	{
		GList<MortarPermission> p = new GList<>();

		for(Field i : getClass().getDeclaredFields())
		{
			if(i.isAnnotationPresent(Permission.class))
			{
				try
				{
					p.add((MortarPermission) i.get(Modifier.isStatic(i.getModifiers()) ? null : this));
				}

				catch(IllegalArgumentException | IllegalAccessException | SecurityException e)
				{
					e.printStackTrace();
				}
			}
		}

		return p;
	}

	public String getFullNode()
	{
		if(hasParent())
		{
			return getParent().getFullNode() + "." + getNode();
		}

		return getNode();
	}

	protected abstract String getNode();

	public abstract String getDescription();

	public abstract boolean isDefault();

	@Override
	public String toString()
	{
		return getFullNode();
	}

	public boolean hasParent()
	{
		return getParent() != null;
	}

	public MortarPermission getParent()
	{
		return parent;
	}

	public void setParent(MortarPermission parent)
	{
		this.parent = parent;
	}
}
