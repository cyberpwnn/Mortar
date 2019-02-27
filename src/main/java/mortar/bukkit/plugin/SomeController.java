package mortar.bukkit.plugin;

public class SomeController extends Controller
{
	@Override
	public void start()
	{
		l("Started");
	}

	@Override
	public void stop()
	{
		l("Stopped");
	}

	@Override
	public void tick()
	{
		l("Ticked");
	}
}
