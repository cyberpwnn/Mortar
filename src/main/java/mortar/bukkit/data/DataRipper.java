package mortar.bukkit.data;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import mortar.api.fulcrum.util.BlocksScraper;
import mortar.util.reflection.V;
import net.minecraft.server.v1_12_R1.DamageSource;
import net.minecraft.server.v1_12_R1.EnchantmentManager;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityLiving;

public class DataRipper
{
	private static BlocksScraper scraper = new BlocksScraper();

	@SuppressWarnings("unchecked")
	public static void forceDeathSequence(LivingEntity e, Entity damager)
	{
		net.minecraft.server.v1_12_R1.Entity entity = ((CraftEntity) damager).getHandle();
		EntityLiving en = ((CraftLivingEntity) e).getHandle();
		if(!en.world.isClientSide)
		{
			int i = 0;
			if(entity instanceof EntityHuman)
			{
				i = EnchantmentManager.g((EntityLiving) entity);
			}

			if(!en.isBaby() && en.world.getGameRules().getBoolean("doMobLoot"))
			{
				new V(en).invoke("a", false, i, DamageSource.CRAMMING);
				ArrayList<org.bukkit.inventory.ItemStack> drops;
				try
				{
					drops = (ArrayList<ItemStack>) en.getClass().getField("drops").get(en);
					for(ItemStack is : drops)
					{
						e.getLocation().getWorld().dropItemNaturally(e.getLocation().clone().add(0, 0.5, 0), is);
					}

					drops.clear();
				}

				catch(IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e1)
				{
					e1.printStackTrace();
				}
			}

			else
			{

			}
		}
	}

	public static void undie(Entity e)
	{
		((CraftEntity) e).getHandle().dead = false;
	}

	public static String getEffectiveToolType(Material material)
	{
		return scraper.getEffectiveTool(material);
	}

	public static int getMinimumToolLevel(Material material)
	{
		return scraper.getMinimumLevel(material);
	}

	public static double getHardness(Material material)
	{
		return scraper.getHardness(material);
	}
}
