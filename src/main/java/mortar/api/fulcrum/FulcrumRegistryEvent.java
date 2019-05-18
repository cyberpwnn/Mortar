package mortar.api.fulcrum;

import mortar.event.MortarEvent;

public class FulcrumRegistryEvent extends MortarEvent
{
	private final FulcrumRegistry registry;

	public FulcrumRegistryEvent()
	{
		this.registry = FulcrumInstance.instance.getRegistry();
	}

	public FulcrumRegistry getRegistry()
	{
		return registry;
	}
}
