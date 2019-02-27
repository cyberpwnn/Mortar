package mortar.bukkit.plugin;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.plugin.Plugin;

import mortar.util.text.C;
import mortar.util.text.TXT;

public class Mortar
{
	public static String tag(String s)
	{
		return TXT.makeTag(C.BLUE, C.DARK_GRAY, C.GRAY, s);
	}

	@SuppressWarnings("unchecked")
	public static <T extends IController> T getController(Class<? extends T> t, Plugin p)
	{
		return (T) ((MortarPlugin) p).getController(t);
	}

	public static <T extends IController> T getController(Class<? extends T> t)
	{
		return getController(t, MortarAPIPlugin.p);
	}

	public static World getDefaultWorld()
	{
		for(World i : Bukkit.getWorlds())
		{
			if(i.getName().equals("world"))
			{
				return i;
			}
		}

		for(World i : Bukkit.getWorlds())
		{
			if(i.getEnvironment().equals(Environment.NORMAL))
			{
				return i;
			}
		}

		return Bukkit.getWorlds().get(0);
	}
}
