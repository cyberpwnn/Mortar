package mortar.bukkit.plugin.commands;

import lombok.Getter;
import lombok.Setter;
import mortar.bukkit.command.MortarSender;

public abstract class DelayedRunnable implements Runnable
{
	@Getter
	@Setter
	private DelayedCommand delayedCommand;

	public DelayedRunnable(DelayedCommand c)
	{
		this.delayedCommand = c;
	}

	public final MortarSender getSender()
	{
		return this.getDelayedCommand().getSender();
	}
}
