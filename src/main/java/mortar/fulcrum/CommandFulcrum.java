package mortar.fulcrum;

import mortar.bukkit.command.Command;
import mortar.bukkit.command.MortarCommand;
import mortar.bukkit.command.MortarSender;
import mortar.bukkit.plugin.MortarAPIPlugin;

public class CommandFulcrum extends MortarCommand
{
	@Command
	private CommandFulcrumRecompile recompile;

	@Command
	private CommandFulcrumFlash flash;

	@Command
	private CommandFulcrumGive give;

	@Command
	private CommandFulcrumList list;

	public CommandFulcrum()
	{
		super("fulcrum", "fu");
		requiresPermission(MortarAPIPlugin.perm);
	}

	@Override
	public boolean handle(MortarSender sender, String[] args)
	{
		sender.sendMessage("/fu compile [-v|-n|-h]");
		sender.sendMessage("/fu flash [<pack> [player] | list]");
		return true;
	}
}
