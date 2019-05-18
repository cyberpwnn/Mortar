package mortar.api.fulcrum.object;

import org.bukkit.Material;

import mortar.api.fulcrum.FulcrumRegistry;
import mortar.api.fulcrum.registry.FCURegisteredObject;
import mortar.api.fulcrum.util.AllocationUnit;
import mortar.api.fulcrum.util.IAllocation;

public abstract class FCUCollective extends FCURegisteredObject implements IAllocation
{
	private short allocationID;
	private AllocationUnit allocationUnit;
	public FCUCollective(String id)
	{
		super(id);
	}

	public abstract void registerResources(FulcrumRegistry registry);

	@Override
	public abstract String getAllocatedModel();

	@Override
	public AllocationUnit getAllocationUnit()
	{
		return allocationUnit;
	}

	@Override
	public short getAllocationID()
	{
		return allocationID;
	}

	@Override
	public Material getAllocationMaterial()
	{
		return getAllocationUnit().getMaterial();
	}

	@Override
	public void onAllocated(AllocationUnit unit, short id)
	{
		this.allocationUnit = unit;
		this.allocationID = id;
	}

	@Override
	public boolean isAllocated()
	{
		return getAllocationUnit() != null;
	}
}
