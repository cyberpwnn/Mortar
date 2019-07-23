package mortar.bukkit.plugin.commands;

import lombok.AccessLevel;
import lombok.Getter;
import mortar.bukkit.command.MortarSender;
import mortar.bukkit.plugin.Controller;
import mortar.lang.collection.GList;

public class DelayedController extends Controller
{

	@Getter(AccessLevel.PROTECTED)
	private GList<DelayedCommand> cmds;

	@Override
	public void start()
	{
		this.cmds = new GList<>();
	}

	@Override
	public void stop()
	{
		this.cmds.clear();
	}

	@Override
	public void tick()
	{
		for (DelayedCommand c : cmds.copy()) {
			if (c.canRun()) {
				c.run();
				cmds.remove(c);
			}
		}
	}

	public DelayedCommand register(DelayedCommand cmd)
	{
		this.cmds.add(cmd);
		return cmd;
	}

	public boolean confirm(MortarSender sender)
	{
		for (DelayedCommand cmd : cmds) {
			if (!cmd.getSender().player().equals(sender.player())) continue;
			cmd.setCancelled(false);
			try {
				cmd.run();
			} catch (Exception ex) {}
			cmds.remove(cmd);
			return true;
		}
		return false;
	}

	public boolean cancel(MortarSender sender)
	{
		for (DelayedCommand cmd : cmds) {
			if (!cmd.getSender().player().equals(sender.player())) continue;
			cmd.setCancelled(true);
			cmd.run();
			cmds.remove(cmd);
			return true;
		}
		return false;
	}
}
