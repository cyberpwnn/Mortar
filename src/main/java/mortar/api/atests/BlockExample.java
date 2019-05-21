package mortar.api.atests;

import org.bukkit.Material;

import mortar.api.fulcrum.object.CustomBlock;
import mortar.api.fulcrum.util.BlockCollision;
import mortar.api.fulcrum.util.BlockSoundCategory;

public class BlockExample extends CustomBlock
{
	public BlockExample(String id)
	{
		super(id);
		setCollisionMode(BlockCollision.FULL);
		setHardnessLike(Material.DIRT);
		setMaxStackSize(64);
		setSound(BlockSoundCategory.BREAK, new SoundSteelBreak());
		setSound(BlockSoundCategory.PLACE, new SoundSteelPlace());
		setSound(BlockSoundCategory.LAND, new SoundSteelLand());
		setSound(BlockSoundCategory.STEP, new SoundSteelStep());
		setSound(BlockSoundCategory.DIG, new SoundSteelDig());
	}
}
