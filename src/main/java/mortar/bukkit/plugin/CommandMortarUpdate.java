package mortar.bukkit.plugin;

import mortar.api.sched.J;
import mortar.bukkit.command.MortarCommand;
import mortar.bukkit.command.MortarSender;

public class CommandMortarUpdate extends MortarCommand
{
	public CommandMortarUpdate()
	{
		super("update", "checkup");
		requiresPermission(MortarAPIPlugin.perm);
	}

	@Override
	public boolean handle(MortarSender sender, String[] args)
	{
		J.a(() -> MortarAPIPlugin.p.checkForUpdates(args.length > 0 && args[0].equalsIgnoreCase("-f")));

		if(sender.isPlayer())
		{
			sender.sendMessage("Check Console for update information");
		}

		return true;
	}
}
