package mortar.api.atests;

import mortar.api.fulcrum.util.DefaultBlockModel;

public class BlockExampleCube extends BlockExample
{
	public BlockExampleCube()
	{
		super("example_cube_all");
		setName("Example [Cube All]");
		setModel(DefaultBlockModel.CUBE_ALL);
		getModel().rewrite("$id", getID());
		setTexture(getID(), "/assets/textures/blocks/steel.png");
	}
}
