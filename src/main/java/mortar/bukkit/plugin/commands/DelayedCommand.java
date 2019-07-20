package mortar.bukkit.plugin.commands;

import lombok.Getter;
import lombok.Setter;
import mortar.bukkit.command.MortarSender;

public class DelayedCommand
{

	@Getter
	@Setter
	private String id;

	@Getter
	private MortarSender sender;

	@Getter
	@Setter
	private Runnable success;

	@Getter
	@Setter
	private Runnable failed;

	@Getter
	@Setter
	private boolean cancelled = true;

	@Getter
	private Long time;

	@Getter
	@Setter
	private Long expire;

	@Setter
	@Getter
	private Long ttl = 5 * 1000L; // 5 second delay

	public DelayedCommand(String id, MortarSender sender, Runnable success)
	{
		this.id = id;
		this.time = System.currentTimeMillis();
		this.expire = this.time + this.ttl;

		this.sender = sender;
		this.success = success;
		this.failed = new DelayedRunnable(this) {
			@Override
			public void run() {
				sender.sendMessage(getId() + " confirmation cancelled");
			}
		};
	}

	public DelayedCommand(String id, MortarSender sender, Runnable success, Runnable failed)
	{
		this(id, sender, success);
		this.failed = failed;
	}

	public void run()
	{
		if (!this.isCancelled() && this.success != null) this.success.run();
		else if (this.isCancelled() && this.failed != null) this.failed.run();
	}

	public boolean canRun()
	{
		return this.expire <= this.time;
	}

}
