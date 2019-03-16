package mortar.bukkit.plugin;

import mortar.api.sound.Instrument;
import mortar.bukkit.command.MortarCommand;
import mortar.bukkit.command.MortarSender;

public class CommandSound extends MortarCommand
{
	public CommandSound()
	{
		super("sound", "inst");
		requiresPermission(MortarAPIPlugin.perm);
	}

	@Override
	public boolean handle(MortarSender sender, String[] args)
	{
		if(args.length == 0)
		{
			for(Instrument i : Instrument.values())
			{
				sender.sendMessage(i.name());
			}
		}

		else
		{
			try
			{
				Instrument.valueOf(args[0].toUpperCase()).play(sender.player());
			}

			catch(Throwable e)
			{
				sender.sendMessage("Not a valid sound");
			}
		}

		return true;
	}

}
