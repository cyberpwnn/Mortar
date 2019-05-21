package mortar.api.fulcrum.util;

import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import mortar.api.fulcrum.FulcrumInstance;
import mortar.api.fulcrum.object.CustomBlock;

public class ToolLevel
{
	public static final int HAND = 0;
	public static final int WOOD = 1;
	public static final int STONE = 2;
	public static final int IRON = 3;
	public static final int DIAMOND = 5;
	public static final int GOLD = 6;

	public static double getMiningSpeed(double h, int l)
	{
		double m = 1;

		if(l > 0)
		{
			m = l * 2D;
		}

		return (1D / ((h * 1.5) / m)) / 20D;
	}

	public static double getMiningSpeed(Block block, ItemStack is)
	{
		double h = 1;

		try
		{
			h = FulcrumInstance.instance.getBlockScraper().getHardness(block);
		}

		catch(Exception e)
		{
			e.printStackTrace();
		}

		int l = ToolLevel.getToolLevel(is);
		int el = 0;

		if(is != null && is.containsEnchantment(Enchantment.DIG_SPEED))
		{
			el = (int) (Math.pow(is.getEnchantmentLevel(Enchantment.DIG_SPEED), 2D) + 1);
		}

		double m = 1;
		double f = 2D;

		if(l > 0)
		{
			if(l > ToolLevel.DIAMOND)
			{
				f = 1.59D;
			}

			m = (l * f) + el;
		}

		return (1D / ((h * 1.5) / m)) / 20D;
	}

	public static double getMiningSpeed(CustomBlock block, ItemStack is)
	{
		double h = block.getHardness();
		int l = ToolLevel.getToolLevel(is);
		int el = 0;

		if(is != null && is.containsEnchantment(Enchantment.DIG_SPEED))
		{
			el = (int) (Math.pow(is.getEnchantmentLevel(Enchantment.DIG_SPEED), 2D) + 1);
		}

		double m = 1;

		if(l > 0)
		{
			m = (l * 2D) + el;
		}

		return (1D / ((h * 1.5) / m)) / 20D;
	}

	public static int getToolLevel(ItemStack is)
	{
		if(is == null)
		{
			return HAND;
		}

		// if(ContentManager.isTool(is))
		// {
		// return ContentManager.getTool(is).getToolLevel();
		// }

		if(ToolType.getType(is).equals(ToolType.HAND))
		{
			return HAND;
		}

		if(is.getType().toString().startsWith("DIAMOND"))
		{
			return DIAMOND;
		}

		if(is.getType().toString().startsWith("IRON"))
		{
			return IRON;
		}

		if(is.getType().toString().startsWith("GOLD"))
		{
			return GOLD;
		}

		if(is.getType().toString().startsWith("WOOD"))
		{
			return WOOD;
		}

		if(is.getType().toString().startsWith("STONE"))
		{
			return STONE;
		}

		return HAND;
	}
}
