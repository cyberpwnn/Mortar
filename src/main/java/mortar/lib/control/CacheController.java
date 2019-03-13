package mortar.lib.control;

import java.io.File;
import java.io.IOException;

import mortar.bukkit.plugin.Controller;
import mortar.logic.io.VIO;

public class CacheController extends Controller
{
	public boolean cached(String key)
	{
		return new File(getCacheFolder(), key).exists();
	}

	public String loadCache(String key) throws IOException
	{
		return VIO.readAll(new File(getCacheFolder(), key));
	}

	public void saveCache(String key, String content) throws IOException
	{
		if(content == null || content.isEmpty())
		{
			new File(getCacheFolder(), key).delete();
			return;
		}

		VIO.writeAll(new File(getCacheFolder(), key), content);
	}

	public File getCacheFolder()
	{
		return new File("cache");
	}

	@Override
	public void start()
	{
		getCacheFolder().mkdirs();
	}

	@Override
	public void stop()
	{

	}

	@Override
	public void tick()
	{

	}
}
