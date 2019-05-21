package mortar.api.fulcrum.util;

import org.bukkit.inventory.ItemStack;

import mortar.lang.collection.GList;

public class PotentialDropList
{
	private GList<PotentialDrop> potentialDrops;

	public PotentialDropList()
	{
		potentialDrops = new GList<>();
	}

	public GList<ItemStack> computeDrops()
	{
		GList<ItemStack> isx = new GList<>();

		for(PotentialDrop i : potentialDrops)
		{
			int m = i.getPotential().amount();

			if(m > 0)
			{
				ItemStack is = i.getItem().clone();
				is.setAmount(m);
				isx.add(is);
			}
		}

		return isx;
	}

	public PotentialDropList add(PotentialDrop drop)
	{
		potentialDrops.add(drop);
		return this;
	}

	public PotentialDropList add(ItemStack is)
	{
		return add(is, new GuaranteedDrop(is.getAmount()));
	}

	public PotentialDropList add(ItemStack is, Potential pot)
	{
		return add(new PotentialDrop(pot, is));
	}

	public GList<PotentialDrop> getPotentialDrops()
	{
		return potentialDrops;
	}
}
