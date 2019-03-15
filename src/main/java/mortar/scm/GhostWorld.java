package mortar.scm;

import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;

import mortar.api.world.BlockType;
import mortar.compute.math.M;
import mortar.lang.collection.GMap;

public class GhostWorld
{
	private GMap<Chunk, ChunkSnapshot> snap;
	private GMap<Chunk, Long> aquired;

	public GhostWorld()
	{
		aquired = new GMap<>();
		snap = new GMap<Chunk, ChunkSnapshot>();
	}

	public void drop(long olderThan)
	{
		for(Chunk i : snap.k())
		{
			if(!aquired.containsKey(i) || M.ms() - aquired.get(i) > olderThan)
			{
				drop(i);
			}
		}
	}

	public void drop(Chunk c)
	{
		snap.remove(c);
		aquired.remove(c);
	}

	@SuppressWarnings("deprecation")
	public BlockType get(Location l)
	{
		Chunk c = l.getChunk();

		if(!snap.containsKey(c))
		{
			snap.put(c, l.getChunk().getChunkSnapshot(false, false, false));
			aquired.put(c, M.ms());
		}

		ChunkSnapshot s = snap.get(c);
		int cxb = l.getBlockX() - ((l.getBlockX() >> 4) << 4);
		int czb = l.getBlockZ() - ((l.getBlockZ() >> 4) << 4);
		return new BlockType(s.getBlockType(cxb, l.getBlockY(), czb), (byte) s.getBlockData(cxb, l.getBlockY(), czb));
	}

	public int size()
	{
		return snap.size();
	}
}
