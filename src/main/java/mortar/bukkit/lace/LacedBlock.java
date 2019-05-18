package mortar.bukkit.lace;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public interface LacedBlock extends Laced
{
	public LacedWorld getLacedWorld();

	public World getWorld();

	public Chunk getChunk();

	public LacedChunk getLacedChunk();

	public Block getBlock();

	public int getX();

	public int getY();

	public int getZ();

	public Location getLocation();
}
