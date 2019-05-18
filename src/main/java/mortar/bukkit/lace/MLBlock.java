package mortar.bukkit.lace;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class MLBlock implements LacedBlock
{
	private Block block;
	private LacedChunk chunk;

	public MLBlock(LacedChunk chunk, Block block)
	{
		this.chunk = chunk;
		this.block = block;
	}

	@Override
	public String getUniqueIdentifier()
	{
		return getLacedChunk().getUniqueIdentifier() + "-block-" + getX() + "-" + getY() + "-" + getZ();
	}

	@Override
	public void commit()
	{
		chunk.commitBlock(this);
	}

	@Override
	public World getWorld()
	{
		return chunk.getWorld();
	}

	@Override
	public LacedWorld getLacedWorld()
	{
		return getLacedChunk().getLacedWorld();
	}

	@Override
	public Chunk getChunk()
	{
		return block.getChunk();
	}

	@Override
	public int getX()
	{
		return block.getX();
	}

	@Override
	public int getZ()
	{
		return block.getZ();
	}

	@Override
	public LacedChunk getLacedChunk()
	{
		return chunk;
	}

	@Override
	public int getY()
	{
		return block.getY();
	}

	@Override
	public Location getLocation()
	{
		return block.getLocation();
	}

	@Override
	public Block getBlock()
	{
		return block;
	}
}
