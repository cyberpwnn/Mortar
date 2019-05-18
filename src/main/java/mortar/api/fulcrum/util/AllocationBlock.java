package mortar.api.fulcrum.util;

import org.bukkit.Material;

import mortar.api.fulcrum.Fulcrum;
import mortar.api.fulcrum.FulcrumRegistry;
import mortar.api.fulcrum.object.FCUModel;
import mortar.api.resourcepack.ModelType;
import mortar.lang.collection.GList;
import mortar.lang.collection.GMap;

public class AllocationBlock
{
	private final GMap<Material, AllocationUnit> allocationUnits;
	private final AllocationStrategy strat;

	public AllocationBlock(AllocationStrategy stat)
	{
		allocationUnits = new GMap<>();
		this.strat = stat;
	}

	public void addDefaultUnits()
	{
		add(Material.DIAMOND_HOE, "diamond_hoe", "items/diamond_hoe", "item/handheld");
		add(Material.IRON_HOE, "iron_hoe", "items/iron_hoe", "item/handheld");
		add(Material.STONE_HOE, "stone_hoe", "items/stone_hoe", "item/handheld");
		add(Material.GOLD_HOE, "golden_hoe", "items/gold_hoe", "item/handheld");
		add(Material.WOOD_HOE, "wooden_hoe", "items/wood_hoe", "item/handheld");
	}

	public void registerAll(FulcrumRegistry registry)
	{
		for(AllocationUnit i : allocationUnits.v())
		{
			if(i.getAllocated() > 0)
			{
				registry.model().register(new FCUModel(i.getModel(), i.generateModel().toString(Fulcrum.minifyJSON ? 0 : 4), ModelType.ITEM));

				if(Fulcrum.generateModelNormals)
				{
					registry.model().register(new FCUModel(i.getModelNormal(), i.generateNormal().toString(Fulcrum.minifyJSON ? 0 : 4), ModelType.ITEM));
				}
			}
		}
	}

	public AllocationUnit getAllocation(Material mat)
	{
		return allocationUnits.get(mat);
	}

	public boolean isAllocated(Material mat)
	{
		return allocationUnits.containsKey(mat);
	}

	public boolean isAllocated(Material mat, short id)
	{
		return getAllocation(mat, id) != null;
	}

	public IAllocation getAllocation(Material mat, short id)
	{
		if(!isAllocated(mat))
		{
			return null;
		}

		return allocationUnits.get(mat).getAllocation(id);
	}

	public int getFreeSpace()
	{
		return getCapacity() - getAllocated();
	}

	public int getCapacity()
	{
		int m = 0;

		for(AllocationUnit i : allocationUnits.v())
		{
			m += i.getCapacity();
		}

		return m;
	}

	public int getAllocated()
	{
		int m = 0;

		for(AllocationUnit i : allocationUnits.v())
		{
			m += i.getAllocated();
		}

		return m;
	}

	public void allocate(IAllocation a)
	{
		nextAvalibleUnit().allocate(a);
	}

	public AllocationUnit nextAvalibleUnit()
	{
		if(strat.equals(AllocationStrategy.SEQUENTIAL))
		{
			for(AllocationUnit i : allocationUnits.v())
			{
				if(i.hasNextID())
				{
					return i;
				}
			}
		}

		else if(strat.equals(AllocationStrategy.CYCLIC))
		{
			GList<AllocationUnit> u = allocationUnits.v();
			int m = getAllocated();

			for(int c = m; c < m + allocationUnits.size(); c++)
			{
				AllocationUnit i = u.get(c % allocationUnits.size());

				if(i.hasNextID())
				{
					return i;
				}
			}
		}

		throw new RuntimeException("Out of allocation capacity!");
	}

	public void add(Material material, String model, String texture, String parent)
	{
		allocationUnits.put(material, new AllocationUnit(material, model, texture, parent));
	}
}
