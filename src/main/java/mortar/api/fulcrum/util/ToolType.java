package mortar.api.fulcrum.util;

import org.bukkit.inventory.ItemStack;

public class ToolType
{
	public static final String PICKAXE = "pickaxe";
	public static final String AXE = "axe";
	public static final String SWORD = "sword";
	public static final String HOE = "hoe";
	public static final String SHEARS = "shears";
	public static final String SHOVEL = "shovel";
	public static final String HAND = "hand";

	public static String getType(ItemStack is)
	{
		if(is == null)
		{
			return HAND;
		}

		// if(ContentManager.isTool(is))
		// {
		// return ContentManager.getTool(is).getToolType();
		// }

		switch(is.getType())
		{
			case DIAMOND_SWORD:
			case GOLD_SWORD:
			case IRON_SWORD:
			case STONE_SWORD:
			case WOOD_SWORD:
				return SWORD;
			case DIAMOND_AXE:
			case GOLD_AXE:
			case IRON_AXE:
			case STONE_AXE:
			case WOOD_AXE:
				return AXE;
			case DIAMOND_HOE:
			case GOLD_HOE:
			case IRON_HOE:
			case STONE_HOE:
			case WOOD_HOE:
				return HOE;
			case DIAMOND_SPADE:
			case GOLD_SPADE:
			case IRON_SPADE:
			case STONE_SPADE:
			case WOOD_SPADE:
				return SHOVEL;
			case DIAMOND_PICKAXE:
			case GOLD_PICKAXE:
			case IRON_PICKAXE:
			case STONE_PICKAXE:
			case WOOD_PICKAXE:
				return PICKAXE;
			case SHEARS:
				return SHEARS;
			default:
				break;
		}

		return HAND;
	}
}
