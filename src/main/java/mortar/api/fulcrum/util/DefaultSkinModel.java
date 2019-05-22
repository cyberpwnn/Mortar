package mortar.api.fulcrum.util;

public enum DefaultSkinModel
{
	C3,
	C6_TOP,
	C6_BOTTOM;

	public String getPath()
	{
		return "/assets/models/inventory/default_inventory_" + name().toLowerCase() + ".json";
	}

	public String getFulcrumPath()
	{
		return "/assets/models/inventory/fulcrum_inventory_" + name().toLowerCase() + ".json";
	}

	public String getName()
	{
		return "fulcrum_inventory_" + name().toLowerCase();
	}
}
