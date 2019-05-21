package mortar.api.fulcrum.registry;

public class FCURegisteredObject implements Registered
{
	protected final String id;

	public FCURegisteredObject(String id)
	{
		this.id = id;
	}

	@Override
	public String getID()
	{
		return id;
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + ":" + id;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
		{
			return true;
		}
		if(obj == null)
		{
			return false;
		}
		if(!(obj instanceof FCURegisteredObject))
		{
			return false;
		}
		FCURegisteredObject other = (FCURegisteredObject) obj;
		if(id == null)
		{
			if(other.id != null)
			{
				return false;
			}
		}
		else if(!id.equals(other.id))
		{
			return false;
		}
		return true;
	}
}
