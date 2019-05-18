package mortar.api.fulcrum;

import java.io.File;

import org.bukkit.event.EventHandler;

import mortar.bukkit.plugin.Controller;

public class FulcrumController extends Controller
{
	@Override
	public void start()
	{
		File folder = new File("fulcrum");

		if(folder.exists())
		{
			new FulcrumInstance().reigsterPack();
		}
	}

	@Override
	public void stop()
	{
		try
		{
			FulcrumInstance.instance.stop();
		}

		catch(Throwable e)
		{

		}
	}

	@Override
	public void tick()
	{

	}

	@EventHandler
	public void on(FulcrumRegistryEvent e)
	{
		e.getRegistry().item().register(new ItemSteelIngot());
		e.getRegistry().item().register(new BlockSteel());
	}
}