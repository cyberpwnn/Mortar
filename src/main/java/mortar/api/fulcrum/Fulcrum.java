package mortar.api.fulcrum;

import org.bukkit.Material;

import mortar.api.fulcrum.util.AllocationStrategy;
import mortar.bukkit.plugin.Mortar;

public class Fulcrum
{
	@FulcrumParameter
	public static int webServerPort = 25541;

	@FulcrumParameter
	public static boolean minifyJSON = true;

	@FulcrumParameter
	public static boolean rebuildOnStartOnly = true;

	@FulcrumParameter
	public static boolean generateModelNormals = true;

	@FulcrumParameter
	public static boolean optimizeImages = true;

	@FulcrumParameter
	public static boolean obfuscate = false;

	@FulcrumParameter
	public static boolean verbose = true;

	@FulcrumParameter
	public static boolean registerExamples = true;

	@FulcrumParameter
	public static boolean deduplicate = true;

	@FulcrumParameter
	public static AllocationStrategy allocationStrategy = AllocationStrategy.CYCLIC;

	public static final Material SOLID_IDLE = Material.BARRIER;

	public FulcrumController getFulcrum()
	{
		return Mortar.getController(FulcrumController.class);
	}
}
