package mortar.fulcrum;

import mortar.api.fulcrum.FulcrumInstance;
import mortar.api.fulcrum.registry.FCURegistrar;
import mortar.api.fulcrum.registry.Registered;
import mortar.bukkit.command.MortarCommand;
import mortar.bukkit.command.MortarSender;
import mortar.bukkit.plugin.MortarAPIPlugin;
import mortar.lang.collection.GMap;

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
		GMap<String, FCURegistrar<? extends Registered>> map = FulcrumInstance.instance.getRegistry().getRegistries();

		if(args.length == 0)
		{
			for(String i : map.k())
			{
				sender.sendMessage(i + " (" + map.get(i).getRegistriesByID().size() + " registered)");
			}

			return true;
		}

		String type = args[0].toLowerCase().trim();

		for(String i : map.k())
		{
			if(type.equalsIgnoreCase(i))
			{
				for(String j : map.get(i).getRegistriesByID().k())
				{
					sender.sendMessage(type + ":" + j);
				}
			}
		}

		return true;
	}
}
