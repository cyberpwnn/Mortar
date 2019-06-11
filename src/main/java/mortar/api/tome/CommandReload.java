package mortar.api.tome;

import mortar.api.sched.J;
import mortar.bukkit.command.MortarCommand;
import mortar.bukkit.command.MortarSender;
import mortar.bukkit.plugin.MortarAPIPlugin;

public class CommandReload extends MortarCommand
{
	public CommandReload()
	{
		super("reload", "relist", "load", "refresh");
		requiresPermission(MortarAPIPlugin.perm);
	}

	@Override
	public boolean handle(MortarSender sender, String[] args)
	{
		J.a(() ->
		{
			TomeLibrary.getInstance().reload();
			sender.sendMessage("Loaded " + TomeLibrary.getInstance().getTomes().size() + " Tomes.");
		});

		return true;
	}

}
