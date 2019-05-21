package mortar.api.atests;

import mortar.api.fulcrum.util.DefaultBlockModel;

public class BlockExampleCauldron extends BlockExample
{
	public BlockExampleCauldron()
	{
		super("example_cauldron");
		setName("Example [Cauldron]");
		setModel(DefaultBlockModel.CAULDRON);
		getModel().rewrite("$id", getID());
		setTexture(getID() + "_outside", "/assets/textures/blocks/steel.png");
		setTexture(getID() + "_inside", "/assets/textures/blocks/quartz.png");
	}
}
