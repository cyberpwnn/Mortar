package mortar.fulcrum;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import mortar.api.fulcrum.FulcrumInstance;
import mortar.api.sched.J;
import mortar.bukkit.command.MortarCommand;
import mortar.bukkit.command.MortarSender;
import mortar.bukkit.plugin.MortarAPIPlugin;
import mortar.compute.math.Profiler;
import mortar.logic.format.F;

public class CommandFulcrumRecompile extends MortarCommand
{
	public CommandFulcrumRecompile()
	{
		super("rebuild", "recompile", "build", "compile");
		requiresPermission(MortarAPIPlugin.perm);
	}

	@Override
	public boolean handle(MortarSender sender, String[] args)
	{
		Profiler px = new Profiler();
		px.begin();
		sender.sendMessage("Building Fulcrum Pack from Registry Cache...");
		J.a(() ->
		{
			try
			{
				FulcrumInstance.instance.rebuild();
				px.end();
				sender.sendMessage("Build Successful: " + F.time(px.getMilliseconds(), 0));
			}

			catch(NoSuchAlgorithmException | IOException e)
			{
				e.printStackTrace();
				sender.sendMessage("Build Failed: " + e.getMessage());
			}
		});

		return true;
	}
}
