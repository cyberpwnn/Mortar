package mortar.api.fulcrum.registry;

import mortar.lang.collection.GList;
import mortar.lang.collection.GMap;

public abstract class FCURegistrar<R extends Registered> implements Registrar<R>
{
	private final GMap<String, R> registered;

	public FCURegistrar()
	{
		registered = new GMap<>();
	}

	@Override
	public GList<R> getRegistries()
	{
		return registered.v();
	}

	@Override
	public GMap<String, R> getRegistriesByID()
	{
		return registered.copy();
	}

	@Override
	public R getRegistry(String id)
	{
		return registered.get(id);
	}

	@Override
	public void register(R r)
	{
		if(isRegistered(r))
		{
			throw new RuntimeException("Object " + r.toString() + " already registered!");
		}

		onRegister(r);
		registered.put(r.getID(), r);
	}

	@Override
	public abstract void onRegister(R r);

	@Override
	public void onUnregister(R r)
	{

	}

	@Override
	public void unregister(String id)
	{
		unregister(getRegistry(id));
	}

	@Override
	public void unregister(R r)
	{
		if(!isRegistered(r))
		{
			throw new RuntimeException("Object " + r.toString() + " is not registered");
		}

		onUnregister(r);
		registered.remove(r.getID());
	}

	@Override
	public void unregisterAll()
	{
		for(R i : getRegistries())
		{
			unregister(i);
		}
	}

	@Override
	public int size()
	{
		return size();
	}

	@Override
	public boolean isRegistered(String id)
	{
		return registered.containsKey(id);
	}

	@Override
	public boolean isRegistered(R r)
	{
		return isRegistered(r.getID());
	}

}
