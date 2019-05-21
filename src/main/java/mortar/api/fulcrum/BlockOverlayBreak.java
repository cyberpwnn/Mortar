package mortar.api.fulcrum;

import mortar.api.fulcrum.object.CustomBlock;
import mortar.api.fulcrum.util.BlockCollision;
import mortar.api.fulcrum.util.DefaultBlockModel;
import mortar.api.fulcrum.util.ToolType;

public class BlockOverlayBreak extends CustomBlock
{
	public BlockOverlayBreak(int stage)
	{
		super("break_stage_" + stage);

		if(stage < 0 || stage > 9)
		{
			throw new RuntimeException("Stage must be 0-9");
		}

		setModel(DefaultBlockModel.CUBE_ALL);
		setTexture(getID(), "/assets/textures/blocks/destroy_stage_" + stage + ".png");
		setHardness(-1);
		setEffectiveToolType(ToolType.HAND);
		setMinimumToolLevel(Integer.MAX_VALUE);
		setCollisionMode(BlockCollision.NONE);
	}

}
