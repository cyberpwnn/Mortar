package mortar.api.fulcrum;

import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import mortar.api.fulcrum.object.FCUBlock;
import mortar.api.fulcrum.util.IAllocation;

public class ContentAssist
{
	public static FCUBlock getBlock(Block b)
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
			return (FCUBlock) al;
		}

		return null;
	}
}
