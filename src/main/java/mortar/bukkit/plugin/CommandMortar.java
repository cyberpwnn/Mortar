package mortar.bukkit.plugin;

import mortar.bukkit.command.MortarCommand;
import mortar.bukkit.command.MortarSender;

public class CommandMortar extends MortarCommand
{
	public CommandMortar()
	{
		super("mortar", "mort", "morty", "mortal");
	}

	@Override
	public boolean handle(MortarSender sender, String[] args)
	{
		sender.sendMessage("I turned myself into a plugin morty!");

		return true;
	}
}
