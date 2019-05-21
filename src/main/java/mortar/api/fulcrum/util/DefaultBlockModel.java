package mortar.api.fulcrum.util;

public enum DefaultBlockModel
{
	CUBE_ALL,
	CUBE_BOTTOM_TOP,
	CUBE_CASED,
	CUBE_COLUMN,
	CUBE_COMPANION,
	CUBE_FRAMED,
	CUBE_MANUAL,
	CUBE_TOP,
	PEDESTAL,
	CAULDRON;

	public String getPath()
	{
		return "/assets/models/block/default_" + name().toLowerCase() + ".json";
	}
}
