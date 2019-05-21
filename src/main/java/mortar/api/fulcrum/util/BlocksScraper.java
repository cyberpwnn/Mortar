package mortar.api.fulcrum.util;

import java.lang.reflect.Field;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;

import mortar.compute.math.Profiler;
import mortar.lang.collection.GMap;
import mortar.logic.format.F;
import mortar.util.text.D;
import net.minecraft.server.v1_12_R1.SoundEffectType;

public class BlocksScraper
{
	private GMap<Material, Double> blockHardness;
	private GMap<Material, Integer> blockMinimums;
	private GMap<Material, String> blockEffectives;
	private GMap<Material, net.minecraft.server.v1_12_R1.Material> blockMaterials;

	public BlocksScraper()
	{
		cacheBlockData();
	}

	public boolean isStone(Material type)
	{
		return CraftMagicNumbers.getBlock(type).getStepSound().equals(SoundEffectType.d);
	}

	public boolean isGlass(Material type)
	{
		return CraftMagicNumbers.getBlock(type).getStepSound().equals(SoundEffectType.f);
	}

	public boolean isMetal(Material type)
	{
		return CraftMagicNumbers.getBlock(type).getStepSound().equals(SoundEffectType.e);
	}

	public boolean isCloth(Material type)
	{
		return CraftMagicNumbers.getBlock(type).getStepSound().equals(SoundEffectType.g);
	}

	public double getHardness(Material t)
	{
		return blockHardness.containsKey(t) ? blockHardness.get(t) : -1;
	}

	public double getHardness(Block t)
	{
		return getHardness(t.getType());
	}

	public int getMinimumLevel(Block b)
	{
		return blockMinimums.containsKey(b.getType()) ? blockMinimums.get(b.getType()) : 0;
	}

	public String getEffectiveTool(Block b)
	{
		String v = blockEffectives.get(b.getType());
		return v == null ? ToolType.HAND : v;
	}

	public boolean shouldDigFaster(Block b, String tool)
	{
		return getEffectiveTool(b).equals(tool);
	}

	public String getEffectiveTool(Material b)
	{
		String v = blockEffectives.get(b);
		return v == null ? ToolType.HAND : v;
	}

	@SuppressWarnings("deprecation")
	public int cacheBlockData()
	{
		try
		{
			Profiler p = new Profiler();
			p.begin();
			blockMinimums = new GMap<Material, Integer>();
			blockEffectives = new GMap<Material, String>();
			blockMaterials = new GMap<Material, net.minecraft.server.v1_12_R1.Material>();
			blockHardness = new GMap<Material, Double>();

			try
			{
				Field strength = net.minecraft.server.v1_12_R1.Block.class.getDeclaredField("strength");
				Field material = net.minecraft.server.v1_12_R1.Block.class.getDeclaredField("material");
				strength.setAccessible(true);
				material.setAccessible(true);

				for(Material i : Material.values())
				{
					if(i.getId() > 255)
					{
						continue;
					}

					net.minecraft.server.v1_12_R1.Block block = net.minecraft.server.v1_12_R1.Block.REGISTRY.getId(i.getId());

					if(block == null)
					{
						continue;
					}

					try
					{
						int level = 0;
						String type = ToolType.HAND;
						blockHardness.put(i, (double) strength.getFloat(block));
						blockMaterials.put(i, (net.minecraft.server.v1_12_R1.Material) material.get(block));
						net.minecraft.server.v1_12_R1.Material m = blockMaterials.get(i);

						if(m.equals(net.minecraft.server.v1_12_R1.Material.WOOD))
						{
							type = ToolType.AXE;
						}

						else if(m.equals(net.minecraft.server.v1_12_R1.Material.BANNER))
						{
							type = ToolType.AXE;
						}

						else if(m.equals(net.minecraft.server.v1_12_R1.Material.CLAY))
						{
							type = ToolType.SHOVEL;
						}

						else if(m.equals(net.minecraft.server.v1_12_R1.Material.CLOTH))
						{
							type = ToolType.SHEARS;
						}

						else if(m.equals(net.minecraft.server.v1_12_R1.Material.CORAL))
						{
							type = ToolType.AXE;
						}

						else if(m.equals(net.minecraft.server.v1_12_R1.Material.DRAGON_EGG))
						{
							type = ToolType.PICKAXE;
						}

						else if(m.equals(net.minecraft.server.v1_12_R1.Material.EARTH))
						{
							type = ToolType.SHOVEL;
						}

						else if(m.equals(net.minecraft.server.v1_12_R1.Material.ICE))
						{
							type = ToolType.PICKAXE;
						}

						else if(m.equals(net.minecraft.server.v1_12_R1.Material.LEAVES))
						{
							type = ToolType.SHEARS;
						}

						else if(m.equals(net.minecraft.server.v1_12_R1.Material.ORE))
						{
							type = ToolType.PICKAXE;
						}

						else if(m.equals(net.minecraft.server.v1_12_R1.Material.PACKED_ICE))
						{
							type = ToolType.PICKAXE;
						}

						else if(m.equals(net.minecraft.server.v1_12_R1.Material.PISTON))
						{
							type = ToolType.PICKAXE;
						}

						else if(m.equals(net.minecraft.server.v1_12_R1.Material.PUMPKIN))
						{
							type = ToolType.AXE;
						}

						else if(m.equals(net.minecraft.server.v1_12_R1.Material.SAND))
						{
							type = ToolType.SHOVEL;
						}

						else if(m.equals(net.minecraft.server.v1_12_R1.Material.SNOW_BLOCK))
						{
							type = ToolType.SHOVEL;
						}

						else if(m.equals(net.minecraft.server.v1_12_R1.Material.SNOW_LAYER))
						{
							type = ToolType.SHOVEL;
						}

						else if(m.equals(net.minecraft.server.v1_12_R1.Material.STONE))
						{
							type = ToolType.PICKAXE;
						}

						else if(m.equals(net.minecraft.server.v1_12_R1.Material.WEB))
						{
							type = ToolType.SWORD;
						}

						else if(m.equals(net.minecraft.server.v1_12_R1.Material.WOOD))
						{
							type = ToolType.AXE;
						}

						else if(m.equals(net.minecraft.server.v1_12_R1.Material.WOOL))
						{
							type = ToolType.SHEARS;
						}

						if(!type.equals(ToolType.HAND) && !type.equals(ToolType.SHEARS))
						{
							switch(i)
							{
								case ANVIL:
									level = ToolLevel.WOOD;
									break;
								case BEACON:
									level = ToolLevel.WOOD;
									break;
								case BLACK_GLAZED_TERRACOTTA:
									level = ToolLevel.WOOD;
									break;
								case BLACK_SHULKER_BOX:
									level = ToolLevel.WOOD;
									break;
								case BLUE_GLAZED_TERRACOTTA:
									level = ToolLevel.WOOD;
									break;
								case BLUE_SHULKER_BOX:
									level = ToolLevel.WOOD;
									break;
								case BONE_BLOCK:
									level = ToolLevel.WOOD;
									break;
								case BREWING_STAND:
									level = ToolLevel.WOOD;
									break;
								case BRICK:
									level = ToolLevel.WOOD;
									break;
								case BRICK_STAIRS:
									level = ToolLevel.WOOD;
									break;
								case BROWN_GLAZED_TERRACOTTA:
									level = ToolLevel.WOOD;
									break;
								case BROWN_SHULKER_BOX:
									level = ToolLevel.WOOD;
									break;
								case BURNING_FURNACE:
									level = ToolLevel.WOOD;
									break;
								case CAULDRON:
									level = ToolLevel.WOOD;
									break;
								case COAL_BLOCK:
									level = ToolLevel.STONE;
									break;
								case COAL_ORE:
									level = ToolLevel.WOOD;
									break;
								case COBBLESTONE:
									level = ToolLevel.WOOD;
									break;
								case COBBLESTONE_STAIRS:
									level = ToolLevel.WOOD;
									break;
								case COBBLE_WALL:
									level = ToolLevel.WOOD;
									break;
								case CONCRETE:
									level = ToolLevel.WOOD;
									break;
								case CYAN_GLAZED_TERRACOTTA:
									level = ToolLevel.WOOD;
									break;
								case CYAN_SHULKER_BOX:
									level = ToolLevel.WOOD;
									break;
								case DAYLIGHT_DETECTOR:
									level = ToolLevel.WOOD;
									break;
								case DAYLIGHT_DETECTOR_INVERTED:
									level = ToolLevel.WOOD;
									break;
								case DIAMOND_BLOCK:
									level = ToolLevel.STONE;
									break;
								case DIAMOND_ORE:
									level = ToolLevel.IRON;
									break;
								case DISPENSER:
									level = ToolLevel.WOOD;
									break;
								case DRAGON_EGG:
									level = ToolLevel.WOOD;
									break;
								case DROPPER:
									level = ToolLevel.WOOD;
									break;
								case EMERALD_BLOCK:
									level = ToolLevel.STONE;
									break;
								case EMERALD_ORE:
									level = ToolLevel.IRON;
									break;
								case ENCHANTMENT_TABLE:
									level = ToolLevel.WOOD;
									break;
								case ENDER_CHEST:
									level = ToolLevel.WOOD;
									break;
								case ENDER_PORTAL_FRAME:
									level = ToolLevel.WOOD;
									break;
								case ENDER_STONE:
									level = ToolLevel.WOOD;
									break;
								case END_BRICKS:
									level = ToolLevel.WOOD;
									break;
								case END_GATEWAY:
									level = ToolLevel.WOOD;
									break;
								case FLOWER_POT:
									level = ToolLevel.WOOD;
									break;
								case FROSTED_ICE:
									level = ToolLevel.WOOD;
									break;
								case FURNACE:
									level = ToolLevel.WOOD;
									break;
								case GOLD_BLOCK:
									level = ToolLevel.STONE;
									break;
								case GOLD_ORE:
									level = ToolLevel.IRON;
									break;
								case GRAY_GLAZED_TERRACOTTA:
									level = ToolLevel.WOOD;
									break;
								case GRAY_SHULKER_BOX:
									level = ToolLevel.WOOD;
									break;
								case GREEN_GLAZED_TERRACOTTA:
									level = ToolLevel.WOOD;
									break;
								case GREEN_SHULKER_BOX:
									level = ToolLevel.WOOD;
									break;
								case HARD_CLAY:
									level = ToolLevel.WOOD;
									break;
								case HOPPER:
									level = ToolLevel.WOOD;
									break;
								case IRON_BLOCK:
									level = ToolLevel.STONE;
									break;
								case IRON_DOOR_BLOCK:
									level = ToolLevel.STONE;
									break;
								case IRON_FENCE:
									level = ToolLevel.WOOD;
									break;
								case IRON_ORE:
									level = ToolLevel.STONE;
									break;
								case IRON_PLATE:
									level = ToolLevel.WOOD;
									break;
								case IRON_TRAPDOOR:
									level = ToolLevel.WOOD;
									break;
								case JUKEBOX:
									level = ToolLevel.WOOD;
									break;
								case LAPIS_BLOCK:
									level = ToolLevel.STONE;
									break;
								case LAPIS_ORE:
									level = ToolLevel.STONE;
									break;
								case LIGHT_BLUE_GLAZED_TERRACOTTA:
									level = ToolLevel.WOOD;
									break;
								case LIGHT_BLUE_SHULKER_BOX:
									level = ToolLevel.WOOD;
									break;
								case LIME_GLAZED_TERRACOTTA:
									level = ToolLevel.WOOD;
									break;
								case LIME_SHULKER_BOX:
									level = ToolLevel.WOOD;
									break;
								case MAGENTA_GLAZED_TERRACOTTA:
									level = ToolLevel.WOOD;
									break;
								case MAGENTA_SHULKER_BOX:
									level = ToolLevel.WOOD;
									break;
								case MAGMA:
									level = ToolLevel.WOOD;
									break;
								case MOB_SPAWNER:
									level = ToolLevel.WOOD;
									break;
								case MOSSY_COBBLESTONE:
									level = ToolLevel.WOOD;
									break;
								case NETHERRACK:
									level = ToolLevel.WOOD;
									break;
								case NETHER_BRICK:
									level = ToolLevel.WOOD;
									break;
								case NETHER_BRICK_ITEM:
									level = ToolLevel.WOOD;
									break;
								case NETHER_BRICK_STAIRS:
									level = ToolLevel.WOOD;
									break;
								case NETHER_FENCE:
									level = ToolLevel.WOOD;
									break;
								case NOTE_BLOCK:
									level = ToolLevel.WOOD;
									break;
								case OBSERVER:
									level = ToolLevel.WOOD;
									break;
								case OBSIDIAN:
									level = ToolLevel.WOOD;
									break;
								case ORANGE_GLAZED_TERRACOTTA:
									level = ToolLevel.WOOD;
									break;
								case ORANGE_SHULKER_BOX:
									level = ToolLevel.WOOD;
									break;
								case PINK_GLAZED_TERRACOTTA:
									level = ToolLevel.WOOD;
									break;
								case PINK_SHULKER_BOX:
									level = ToolLevel.WOOD;
									break;
								case PISTON_BASE:
									level = ToolLevel.WOOD;
									break;
								case PISTON_EXTENSION:
									level = ToolLevel.WOOD;
									break;
								case PISTON_MOVING_PIECE:
									level = ToolLevel.WOOD;
									break;
								case PISTON_STICKY_BASE:
									level = ToolLevel.WOOD;
									break;
								case PRISMARINE:
									level = ToolLevel.WOOD;
									break;
								case PRISMARINE_CRYSTALS:
									level = ToolLevel.WOOD;
									break;
								case PRISMARINE_SHARD:
									level = ToolLevel.WOOD;
									break;
								case PURPLE_GLAZED_TERRACOTTA:
									level = ToolLevel.WOOD;
									break;
								case PURPLE_SHULKER_BOX:
									level = ToolLevel.WOOD;
									break;
								case PURPUR_BLOCK:
									level = ToolLevel.WOOD;
									break;
								case PURPUR_DOUBLE_SLAB:
									level = ToolLevel.WOOD;
									break;
								case PURPUR_PILLAR:
									level = ToolLevel.WOOD;
									break;
								case PURPUR_SLAB:
									level = ToolLevel.WOOD;
									break;
								case PURPUR_STAIRS:
									level = ToolLevel.WOOD;
									break;
								case QUARTZ_BLOCK:
									level = ToolLevel.STONE;
									break;
								case QUARTZ_ORE:
									level = ToolLevel.WOOD;
									break;
								case QUARTZ_STAIRS:
									level = ToolLevel.WOOD;
									break;
								case REDSTONE_BLOCK:
									level = ToolLevel.STONE;
									break;
								case REDSTONE_LAMP_OFF:
									level = ToolLevel.WOOD;
									break;
								case REDSTONE_LAMP_ON:
									level = ToolLevel.WOOD;
									break;
								case REDSTONE_ORE:
									level = ToolLevel.IRON;
									break;
								case RED_GLAZED_TERRACOTTA:
									level = ToolLevel.WOOD;
									break;
								case RED_MUSHROOM:
									level = ToolLevel.WOOD;
									break;
								case RED_NETHER_BRICK:
									level = ToolLevel.WOOD;
									break;
								case RED_SANDSTONE:
									level = ToolLevel.WOOD;
									break;
								case RED_SANDSTONE_STAIRS:
									level = ToolLevel.WOOD;
									break;
								case RED_SHULKER_BOX:
									level = ToolLevel.WOOD;
									break;
								case SANDSTONE:
									level = ToolLevel.WOOD;
									break;
								case SANDSTONE_STAIRS:
									level = ToolLevel.WOOD;
									break;
								case SEA_LANTERN:
									level = ToolLevel.WOOD;
									break;
								case SHULKER_SHELL:
									level = ToolLevel.WOOD;
									break;
								case SILVER_GLAZED_TERRACOTTA:
									level = ToolLevel.WOOD;
									break;
								case SILVER_SHULKER_BOX:
									level = ToolLevel.WOOD;
									break;
								case SMOOTH_BRICK:
									level = ToolLevel.WOOD;
									break;
								case SMOOTH_STAIRS:
									level = ToolLevel.WOOD;
									break;
								case SNOW:
									level = ToolLevel.WOOD;
									break;
								case SNOW_BLOCK:
									level = ToolLevel.WOOD;
									break;
								case STAINED_CLAY:
									level = ToolLevel.WOOD;
									break;
								case STEP:
									level = ToolLevel.WOOD;
									break;
								case STONE:
									level = ToolLevel.WOOD;
									break;
								case STONE_PLATE:
									level = ToolLevel.WOOD;
									break;
								case STONE_SLAB2:
									level = ToolLevel.WOOD;
									break;
								case WEB:
									level = ToolLevel.WOOD;
									break;
								case WHITE_GLAZED_TERRACOTTA:
									level = ToolLevel.WOOD;
									break;
								case WHITE_SHULKER_BOX:
									level = ToolLevel.WOOD;
									break;
								case YELLOW_GLAZED_TERRACOTTA:
									level = ToolLevel.WOOD;
									break;
								case YELLOW_SHULKER_BOX:
									level = ToolLevel.WOOD;
									break;
								default:
									break;
							}
						}

						blockMinimums.put(i, level);
						blockEffectives.put(i, type);
					}

					catch(Throwable e)
					{
						e.printStackTrace();
					}
				}
			}

			catch(Throwable e)
			{
				e.printStackTrace();
			}

			p.end();
			D.as(this).l("Cached " + F.f(blockHardness.size()) + " vanilla block types in " + F.time(p.getMilliseconds(), 1));
		}

		catch(Throwable e)
		{
			e.printStackTrace();
		}

		return blockHardness.size();
	}
}
