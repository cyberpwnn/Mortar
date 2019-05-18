package mortar.bukkit.lace;

import org.bukkit.Chunk;
import org.bukkit.World;

import mortar.lang.collection.GList;

public interface LacedWorld extends Laced
{
	public String getWorldName();

	public World getWorld();

	public LacedChunk getLacedChunk(Chunk c);

	public LacedBlock getBlock(int x, int y, int z);

	public GList<LacedChunk> getChunksLoaded();

	public void commitChunk(MLChunk mlChunk);
}
