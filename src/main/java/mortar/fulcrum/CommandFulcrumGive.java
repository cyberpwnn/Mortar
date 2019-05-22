package mortar.fulcrum;

import mortar.api.fulcrum.FulcrumInstance;
import mortar.api.fulcrum.object.CustomInventory;
import mortar.api.fulcrum.object.CustomItem;
import mortar.api.fulcrum.object.CustomSound;
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

		if(type.equals("item") || type.equals("block") || type.equals("skin"))
		{
			CustomItem item = (CustomItem) FulcrumInstance.instance.getRegistry().collective().getRegistry(id);

			if(item != null)
			{
				sender.player().getInventory().addItem(item.toItemStack(item.getMaxStackSize()));
			}

			else
			{
				sender.sendMessage("Cannot find " + id);
			}
		}

		else if(type.equals("inventory"))
		{
			CustomInventory inv = FulcrumInstance.instance.getRegistry().inventory().getRegistry(id);

			if(inv != null)
			{
				inv.showWindow(sender.player());
			}

			else
			{
				sender.sendMessage("Cannot find " + id);
			}
		}

		else if(type.equals("sound"))
		{
			CustomSound sound = FulcrumInstance.instance.getRegistry().sound().getRegistry(id);

			if(sound != null)
			{
				sound.constructAudible().play(sender.player());
			}

			else
			{
				sender.sendMessage("Cannot find " + id);
			}
		}

		else
		{
			sender.sendMessage("Try /fu list");
		}

		return true;
	}
}
