package mortar.api.atests;

import mortar.api.fulcrum.object.CustomInventory;
import mortar.api.fulcrum.util.InventorySkinType;

public class InventoryExampleChestDouble extends CustomInventory
{
	public InventoryExampleChestDouble()
	{
		super("example_chest_double");
		setName("Example Double Chest");
		setSkinType(InventorySkinType.W9_H6);
		setSkinTexture("/assets/textures/inventories/chest_6/layout.png");
	}
}
