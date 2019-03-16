package mortar.bukkit.plugin;

import mortar.bukkit.command.Command;
import mortar.bukkit.command.MortarCommand;
import mortar.bukkit.command.MortarSender;

public class CommandMortar extends MortarCommand
{
	@Command
	private CommandMortarUpdate update;

	@Command
	private CommandSound sound;

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
