package mortar.api.atests;

import mortar.api.fulcrum.util.DefaultBlockModel;

public class BlockExamplePedestal extends BlockExample
{
	public BlockExamplePedestal()
	{
		super("example_cube_pedestal");
		setName("Example [Cube Pedestal]");
		setModel(DefaultBlockModel.PEDESTAL);
		getModel().rewrite("$id", getID());
		setTexture(getID() + "_top", "/assets/textures/blocks/steel.png");
		setTexture(getID() + "_bottom", "/assets/textures/blocks/steel.png");
		setTexture(getID() + "_pillar", "/assets/textures/blocks/quartz.png");
	}
}
