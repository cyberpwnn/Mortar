package mortar.api.fulcrum.registry;

import mortar.lang.collection.GList;
import mortar.lang.collection.GMap;

public interface Registrar<R extends Registered>
{
	public boolean isRegistered(String id);

	public boolean isRegistered(R r);

	public GList<R> getRegistries();

	public GMap<String, R> getRegistriesByID();

	public R getRegistry(String id);

	public void register(R r);

	public void onRegister(R r);

	public void onUnregister(R r);

	public void unregister(String id);

	public void unregister(R r);

	public void unregisterAll();

	public int size();
}
