package mortar.api.fulcrum.util;

import org.bukkit.Material;

import mortar.api.fulcrum.object.FCUBlock;

public interface IAllocation
{
	public String getAllocatedModel();

	public AllocationUnit getAllocationUnit();

	public short getAllocationID();

	public Material getAllocationMaterial();

	public boolean isAllocated();

	public void onAllocated(AllocationUnit unit, short id);

	public FCUBlock block();

	public boolean isBlock();

	public boolean isItem();
}
