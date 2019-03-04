package mortar.bukkit.plugin;

import mortar.api.config.Key;

public class MortarConfig
{
	@Key("debug.agressive-logging")
	public static boolean DEBUG_LOGGING = false;

	@Key("updates.update-automatically")
	public static boolean UPDATES = true;
}
