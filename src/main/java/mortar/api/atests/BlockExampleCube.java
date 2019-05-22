package mortar.api.atests;

import org.bukkit.Material;

import mortar.api.fulcrum.util.DefaultBlockModel;
import mortar.api.fulcrum.util.ToolLevel;
import mortar.api.fulcrum.util.ToolType;

public class BlockExampleCube extends BlockExample
{
	public BlockExampleCube()
	{
		super("example_cube_all");
		setName("Example [Cube All]");
		setModel(DefaultBlockModel.CUBE_ALL);
		getModel().rewrite("$id", getID());
		setTexture(getID(), "/assets/textures/blocks/steel.png");
		setHardnessLike(Material.IRON_BLOCK);
		setMinimumToolLevel(ToolLevel.STONE);
		setEffectiveToolType(ToolType.PICKAXE);
	}
}
