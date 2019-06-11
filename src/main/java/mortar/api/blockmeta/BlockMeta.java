package mortar.api.blockmeta;

import org.bukkit.block.Block;

import mortar.api.config.Ignore;

public class BlockMeta
{
	@Ignore
	public String world;

	@Ignore
	public int x;

	@Ignore
	public int y;

	@Ignore
	public int z;

	public BlockMeta()
	{

	}

	public BlockMeta(Block block)
	{
		this.world = block.getWorld().getName();
		this.x = block.getX();
		this.y = block.getY();
		this.z = block.getZ();
	}

	public void load()
	{

	}

	public void save()
	{

	}
}
