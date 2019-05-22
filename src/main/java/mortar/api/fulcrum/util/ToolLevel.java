package mortar.api.fulcrum.util;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import mortar.api.fulcrum.object.CustomBlock;

public class ToolLevel
{
	public static final int HAND = 0;
	public static final int WOOD = 1;
	public static final int STONE = 2;
	public static final int IRON = 3;
	public static final int DIAMOND = 5;
	public static final int GOLD = 6;

	public static double getMiningSpeed(Player p, CustomBlock block, ItemStack is)
	{
		String t = ToolType.getType(is);
		String o = block.getEffectiveToolType();
		double h = block.getHardness();
		int l = ToolLevel.getToolLevel(is);
		int i = block.getMinimumToolLevel();
		int el = 0;
		double pmod = 1;

		if(is != null && is.containsEnchantment(Enchantment.DIG_SPEED))
		{
			el = (int) (Math.pow(is.getEnchantmentLevel(Enchantment.DIG_SPEED), 2D) + 1);
		}

		double m = 1;

		if(l > 0 && t.equals(o) && l >= i)
		{
			m = (l * 2D) + el;
			if(p.hasPotionEffect(PotionEffectType.SLOW_DIGGING))
			{
				pmod -= (p.getPotionEffect(PotionEffectType.SLOW_DIGGING).getAmplifier() + 1D) * 0.1;
			}

			if(p.hasPotionEffect(PotionEffectType.FAST_DIGGING))
			{
				pmod += (p.getPotionEffect(PotionEffectType.FAST_DIGGING).getAmplifier() + 1D) * 0.1;
			}

			pmod = pmod < 0 ? 0.01 : pmod;
		}

		else
		{
			pmod = 0.35;
		}

		return (((1D / ((h * 1.5) / m)) / 20D) * pmod);
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
