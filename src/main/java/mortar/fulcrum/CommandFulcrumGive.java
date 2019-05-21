package mortar.fulcrum;

import mortar.api.fulcrum.FulcrumInstance;
import mortar.api.fulcrum.object.CustomItem;
import mortar.bukkit.command.MortarCommand;
import mortar.bukkit.command.MortarSender;
import mortar.bukkit.plugin.MortarAPIPlugin;

public class CommandFulcrumGive extends MortarCommand
{
	public CommandFulcrumGive()
	{
		super("give");
		requiresPermission(MortarAPIPlugin.perm);
	}

	@Override
	public boolean handle(MortarSender sender, String[] args)
	{
		if(args.length == 0 || !args[0].contains(":"))
		{
			sender.sendMessage("/fu give type:id");
			sender.sendMessage("i.e. /fu give item:steel_ingot");
		}

		String tag = args[0].toLowerCase().trim();
		String type = tag.split(":")[0];
		String id = tag.split(":")[1];

		if(type.equals("item"))
		{
			CustomItem item = FulcrumInstance.instance.getRegistry().item().getRegistry(id);

			if(item != null)
			{
				sender.player().getInventory().addItem(item.toItemStack(item.getMaxStackSize()));
			}

			else
			{
				sender.sendMessage("Cannot find " + id);
			}
		}

		return true;
	}
}
