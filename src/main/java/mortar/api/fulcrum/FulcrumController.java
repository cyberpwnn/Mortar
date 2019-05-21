package mortar.api.fulcrum;

import java.io.File;
import java.io.IOException;

import org.bukkit.event.EventHandler;

import mortar.api.atests.BlockExampleCased;
import mortar.api.atests.BlockExampleCauldron;
import mortar.api.atests.BlockExampleCompanion;
import mortar.api.atests.BlockExampleCube;
import mortar.api.atests.BlockExampleFramed;
import mortar.api.atests.BlockExamplePedestal;
import mortar.bukkit.plugin.Controller;
import mortar.lang.json.JSONException;

public class FulcrumController extends Controller
{
	@Override
	public void start()
	{
		File folder = new File("fulcrum");

		if(folder.exists())
		{
			try
			{
				new FulcrumInstance().reigsterPack();
			}

			catch(JSONException | IOException e)
			{
				e.printStackTrace();
			}
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
		e.getRegistry().item().register(new BlockExampleCube());
		e.getRegistry().item().register(new BlockExampleFramed());
		e.getRegistry().item().register(new BlockExampleCased());
		e.getRegistry().item().register(new BlockExampleCompanion());
		e.getRegistry().item().register(new BlockExampleCauldron());
		e.getRegistry().item().register(new BlockExamplePedestal());
	}
}