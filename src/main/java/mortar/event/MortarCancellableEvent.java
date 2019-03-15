package mortar.event;

import org.bukkit.event.Cancellable;

public class MortarCancellableEvent extends MortarEvent implements Cancellable
{
	private boolean cancelled = false;

	@Override
	public boolean isCancelled()
	{
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled)
	{
		this.cancelled = cancelled;
	}
}
