package mortar.bukkit.plugin;

import mortar.bukkit.command.MortarCommand;
import mortar.bukkit.command.MortarSender;

public class CommandMortar extends MortarCommand
{
	public CommandMortar()
	{
		super("mortar", "mort", "morty", "mortal", "mtr");
		requiresPermission(MortarAPIPlugin.perm);
	}

	@Override
	public boolean handle(MortarSender sender, String[] args)
	{
		sender.sendMessage("v" + MortarAPIPlugin.p.getDescription().getVersion());

		return true;
	}
}
