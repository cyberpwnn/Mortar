package mortar.api.fulcrum.util;

public enum DefaultItemModel
{
	ITEM,
	TOOL;

	public String getPath()
	{
		return "/assets/models/item/default_" + name().toLowerCase() + ".json";
	}
}
