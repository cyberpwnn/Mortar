package mortar.fulcrum;

import mortar.bukkit.command.MortarCommand;
import mortar.bukkit.command.MortarSender;
import mortar.bukkit.plugin.MortarAPIPlugin;

public class CommandFulcrumRecompile extends MortarCommand
{
	public CommandFulcrumRecompile()
	{
		super("recompile");
		requiresPermission(MortarAPIPlugin.perm);
	}

	@Override
	public boolean handle(MortarSender sender, String[] args)
	{

		return true;
	}
}
