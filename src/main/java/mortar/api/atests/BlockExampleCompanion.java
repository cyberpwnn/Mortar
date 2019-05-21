package mortar.api.atests;

import mortar.api.fulcrum.util.DefaultBlockModel;

public class BlockExampleCompanion extends BlockExample
{
	public BlockExampleCompanion()
	{
		super("example_cube_companion");
		setName("Example [Cube Companion]");
		setModel(DefaultBlockModel.CUBE_COMPANION);
		getModel().rewrite("$id", getID());
		setTexture(getID() + "_outside", "/assets/textures/blocks/steel.png");
		setTexture(getID() + "_inside", "/assets/textures/blocks/quartz.png");
	}
}
