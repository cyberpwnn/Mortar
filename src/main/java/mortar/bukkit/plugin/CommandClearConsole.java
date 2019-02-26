package mortar.bukkit.plugin;

import mortar.bukkit.command.MortarCommand;
import mortar.bukkit.command.MortarSender;
import mortar.logic.format.F;

public class CommandClearConsole extends MortarCommand
{
	public CommandClearConsole()
	{
		super("cls");
	}

	@Override
	public boolean handle(MortarSender sender, String[] args)
	{
		if(sender.isPlayer())
		{
			return false;
		}

		sender.sendMessage(F.repeat("\n ", 80));
		sender.sendMessage("Poof");
		return true;
	}
}
