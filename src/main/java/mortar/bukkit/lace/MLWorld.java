package mortar.bukkit.lace;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;

import mortar.lang.collection.GList;
import mortar.lang.collection.GMap;

public class MLWorld implements LacedWorld
{
	private World world;
	private GMap<Chunk, LacedChunk> laced;

	public MLWorld(World world)
	{
		this.world = world;
		laced = new GMap<>();
	}

	@Override
	public String getUniqueIdentifier()
	{
		return "world-" + world.getName();
	}

	@Override
	public void commit()
	{
		// Modified
	}

	@Override
	public String getWorldName()
	{
		return world.getName();
	}

	@Override
	public World getWorld()
	{
		return world;
	}

	@Override
	public LacedChunk getLacedChunk(Chunk c)
	{
		if(!laced.containsKey(c))
		{
			// PUT
		}

		return laced.get(c);
	}

	@Override
	public LacedBlock getBlock(int x, int y, int z)
	{
		Block b = null;
		return getLacedChunk((b = getWorld().getBlockAt(x, y, z)).getChunk()).getBlock(b);
	}

	@Override
	public GList<LacedChunk> getChunksLoaded()
	{
		return laced.v();
	}

	@Override
	public void commitChunk(MLChunk mlChunk)
	{
		commit();
	}
}
