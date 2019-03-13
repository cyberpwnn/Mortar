package mortar.api.nms;

import org.bukkit.Chunk;
import org.bukkit.Location;

import mortar.lang.collection.GMap;
import mortar.lang.collection.GSet;

public class ChunkTracker
{
	private GMap<Chunk, GSet<Integer>> ig;

	public ChunkTracker()
	{
		ig = new GMap<Chunk, GSet<Integer>>();
	}

	public void hit(Location l)
	{
		if(!ig.containsKey(l.getChunk()))
		{
			ig.put(l.getChunk(), new GSet<Integer>());
		}

		ig.get(l.getChunk()).add(l.getBlockY() >> 4);
	}

	public void flush()
	{
		for(Chunk i : ig.k())
		{
			for(int j : ig.get(i))
			{
				NMP.CHUNK.resend(i.getWorld(), i.getX(), j, i.getZ());
			}
		}

		ig.clear();
	}
}
