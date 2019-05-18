package mortar.fulcrum;

import mortar.api.fulcrum.FulcrumInstance;
import mortar.api.fulcrum.util.ResourcePackUtil;
import mortar.bukkit.command.MortarCommand;
import mortar.bukkit.command.MortarSender;
import mortar.bukkit.plugin.MortarAPIPlugin;

public class CommandFulcrumFlash extends MortarCommand
{
	public CommandFulcrumFlash()
	{
		super("flash");
		requiresPermission(MortarAPIPlugin.perm);
	}

	@Override
	public boolean handle(MortarSender sender, String[] args)
	{
		new ResourcePackUtil().sendResourcePackWeb(sender.player(), FulcrumInstance.packName + ".zip");

		return true;
	}
}
