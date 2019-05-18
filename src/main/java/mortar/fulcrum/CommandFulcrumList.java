package mortar.fulcrum;

import mortar.api.fulcrum.FulcrumInstance;
import mortar.api.fulcrum.object.FCUItem;
import mortar.bukkit.command.MortarCommand;
import mortar.bukkit.command.MortarSender;
import mortar.bukkit.plugin.MortarAPIPlugin;

public class CommandFulcrumList extends MortarCommand
{
	public CommandFulcrumList()
	{
		super("list");
		requiresPermission(MortarAPIPlugin.perm);
	}

	@Override
	public boolean handle(MortarSender sender, String[] args)
	{
		for(FCUItem i : FulcrumInstance.instance.getRegistry().item().getRegistries())
		{
			sender.sendMessage("item:" + i.getID());
		}

		return true;
	}
}
