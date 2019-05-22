package mortar.api.fulcrum;

import java.io.File;
import java.io.IOException;

import mortar.api.fulcrum.util.EntityNMS12;
import mortar.api.fulcrum.util.EntityNMS12.Type;
import mortar.api.sched.J;
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
			EntityNMS12.registerEntity("block_stand", Type.ARMOR_STAND, BlockStand12.class);

			J.s(() ->
			{
				try
				{
					new FulcrumInstance().reigsterPack();
				}

				catch(JSONException | IOException e)
				{
					e.printStackTrace();
				}
			});
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
}