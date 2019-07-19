package mortar.api.fulcrum;

import mortar.bukkit.compatibility.MaterialEnum;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import mortar.api.fulcrum.object.CustomBlock;
import mortar.api.fulcrum.object.CustomItem;
import mortar.api.fulcrum.util.FCUID;
import mortar.api.fulcrum.util.IAllocation;
import mortar.api.sparse.SparseProperties;
import mortar.util.text.D;

public class ContentAssist
{
	public static void validate(Block block)
	{
		validate(getArmorStand(block));
	}

	public static void validate(ArmorStand a)
	{
		if(a != null)
		{
			a.setHelmet(validate(a.getHelmet()));
		}
	}

	public static void validate(Item item)
	{
		item.setItemStack(validate(item.getItemStack()));
	}

	public static void validate(Inventory inventory)
	{
		ItemStack[] is = inventory.getContents();

		for(int i = 0; i < is.length; i++)
		{
			is[i] = validate(is[i]);
		}

		inventory.setContents(is);
	}

	public static ItemStack validate(ItemStack is)
	{
		if(is == null)
		{
			return is;
		}

		if(is.getType().equals(MaterialEnum.AIR.bukkitMaterial()))
		{
			return is;
		}

		ItemMeta im = is.getItemMeta();

		if(!im.isUnbreakable() || is.getDurability() == 0)
		{
			return is;
		}

		Material type = is.getType();
		short dura = is.getDurability();
		SparseProperties p = SparseProperties.from(is);

		if(p.isEmpty())
		{
			return is;
		}

		if(p.contains("fcuid"))
		{
			FCUID id = p.get("fcuid", FCUID.class);
			IAllocation a = getAllocation(id.getId());

			if(a != null)
			{
				if(type.equals(a.getAllocationMaterial()) && dura == a.getAllocationID())
				{
					// Valid
					return is;
				}

				else
				{
					// Invalid, but we have the registry avalible
					// Converting is considered safe here.
					ItemStack isr = ((CustomItem) a).toItemStack(is.getAmount());
					isr.addUnsafeEnchantments(is.getEnchantments());
					ItemMeta imx = isr.getItemMeta();
					imx.setLore(im.getLore());
					imx.setDisplayName(im.getDisplayName());
					isr.setItemMeta(imx);

					D.as("ContentAssist").v("Converted Item: " + is.getType() + ":" + is.getDurability() + " -> " + isr.getType() + ":" + isr.getDurability() + " (" + id.getId() + " allocation changed)");

					return isr;
				}
			}

			else
			{
				// This item is invalid (not reconized by fcu)
				// However we dont know if we have any other registries
				// Assuming data just isnt avalible right now
				// So we wont deleted, we will just keep it broken
				return is;
			}
		}

		return is;
	}

	public static IAllocation getAllocation(String id)
	{
		return FulcrumInstance.instance.getRegistered(id);
	}

	public static IAllocation getAllocation(ItemStack is)
	{
		return FulcrumInstance.instance.getRegistered(is);
	}

	public static CustomBlock getBlock(Block b)
	{
		ArmorStand a = null;

		for(Entity i : b.getWorld().getNearbyEntities(b.getLocation().clone().add(0.5, 0.5, 0.5), 1, 1, 1))
		{
			if(i instanceof ArmorStand)
			{
				a = (ArmorStand) i;
				break;
			}
		}

		if(a == null)
		{
			return null;
		}

		IAllocation al = FulcrumInstance.instance.getRegistered(a.getHelmet());

		if(al != null)
		{
			return (CustomBlock) al;
		}

		return null;
	}

	public static ArmorStand getArmorStand(Block b)
	{
		ArmorStand a = null;

		for(Entity i : b.getWorld().getNearbyEntities(b.getLocation().clone().add(0.5, 0.5, 0.5), 1, 1, 1))
		{
			if(i instanceof ArmorStand)
			{
				a = (ArmorStand) i;
				break;
			}
		}

		if(a == null)
		{
			return null;
		}

		return a;
	}
}
