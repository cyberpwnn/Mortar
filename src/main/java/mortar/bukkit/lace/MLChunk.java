package mortar.bukkit.lace;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;

import mortar.lang.collection.GList;
import mortar.lang.collection.GMap;

public class MLChunk implements LacedChunk
{
	private Chunk chunk;
	private LacedWorld world;
	private GMap<Block, LacedBlock> laced;

	public MLChunk(LacedWorld world, Chunk chunk)
	{
		this.world = world;
		this.chunk = chunk;
		laced = new GMap<>();
	}

	@Override
	public String getUniqueIdentifier()
	{
		return getLacedWorld().getUniqueIdentifier() + "-chunk-" + getX() + "-" + getZ();
	}

	@Override
	public void commit()
	{
		getLacedWorld().commitChunk(this);
	}

	@Override
	public World getWorld()
	{
		return chunk.getWorld();
	}

	@Override
	public LacedWorld getLacedWorld()
	{
		return world;
	}

	@Override
	public Chunk getChunk()
	{
		return chunk;
	}

	@Override
	public int getX()
	{
		return getChunk().getX();
	}

	@Override
	public int getZ()
	{
		return getChunk().getZ();
	}

	@Override
	public LacedBlock getBlock(int x, int y, int z)
	{
		Block b = getChunk().getBlock(x, y, z);

		if(!laced.containsKey(b))
		{
			// put
		}

		return laced.get(b);
	}

	@Override
	public LacedBlock getBlock(Block b)
	{
		if(!b.getChunk().equals(chunk))
		{
			throw new RuntimeException("Block does not belong to chunk");
		}

		if(!laced.containsKey(b))
		{
			// put
		}

		return laced.get(b);
	}

	@Override
	public GList<LacedBlock> getBlocks()
	{
		return laced.v();
	}

	@Override
	public void commitBlock(MLBlock mlBlock)
	{
		getLacedWorld().commitChunk(this);
	}
}
