package mortar.api.fx;

import mortar.api.world.BlockType;

public interface TypeEffect
{
	public TypeEffect setType(BlockType type);

	public BlockType getType();
}
