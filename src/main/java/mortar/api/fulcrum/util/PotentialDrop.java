package mortar.api.fulcrum.util;

import org.bukkit.inventory.ItemStack;

public class PotentialDrop
{
	private Potential potential;
	private ItemStack item;

	public PotentialDrop(Potential potential, ItemStack item)
	{
		this.potential = potential;
		this.item = item;
	}

	public Potential getPotential()
	{
		return potential;
	}

	public void setPotential(Potential potential)
	{
		this.potential = potential;
	}

	public ItemStack getItem()
	{
		return item;
	}

	public void setItem(ItemStack item)
	{
		this.item = item;
	}
}
