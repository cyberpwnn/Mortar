package mortar.bukkit.plugin;

import mortar.bukkit.command.MortarPermission;

public class PermissionMortar extends MortarPermission
{
	@Override
	protected String getNode()
	{
		return "mortar";
	}

	@Override
	public String getDescription()
	{
		return "Mortar permissions";
	}

	@Override
	public boolean isDefault()
	{
		return false;
	}
}
