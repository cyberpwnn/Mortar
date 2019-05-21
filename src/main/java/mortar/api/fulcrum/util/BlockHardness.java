package mortar.api.fulcrum.util;

import org.bukkit.Material;

import mortar.api.fulcrum.FulcrumInstance;

public class BlockHardness
{
	public static double getHardness(Material m)
	{
		return FulcrumInstance.instance.getBlockScraper().getHardness(m);
	}

	public static String getEffectiveTool(Material m)
	{
		return FulcrumInstance.instance.getBlockScraper().getEffectiveTool(m);
	}
}
