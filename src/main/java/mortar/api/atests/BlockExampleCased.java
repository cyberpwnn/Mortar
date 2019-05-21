package mortar.api.atests;

import mortar.api.fulcrum.util.DefaultBlockModel;

public class BlockExampleCased extends BlockExample
{
	public BlockExampleCased()
	{
		super("example_cube_cased");
		setName("Example [Cube Cased]");
		setModel(DefaultBlockModel.CUBE_CASED);
		getModel().rewrite("$id", getID());
		setTexture(getID() + "_outside", "/assets/textures/blocks/steel.png");
		setTexture(getID() + "_inside", "/assets/textures/blocks/quartz.png");
	}
}
