package mortar.api.tome;

import mortar.bukkit.command.Command;
import mortar.bukkit.command.MortarCommand;
import mortar.bukkit.command.MortarSender;
import mortar.bukkit.plugin.MortarAPIPlugin;

public class CommandTome extends MortarCommand
{
	@Command
	public CommandReload reload;

	@Command
	public CommandGive give;

	@Command
	public CommandCatalogue cat;

	public CommandTome()
	{
		super("tome");
		requiresPermission(MortarAPIPlugin.perm);
	}

	@Override
	public boolean handle(MortarSender sender, String[] args)
	{
		sender.sendMessage("/tome catalogue");
		sender.sendMessage("/tome reload");
		sender.sendMessage("/tome give <id> [-d|--disk]");

		return true;
	}

}
