package mortar.api.generator;

import java.util.Random;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

public class VoidGenerator extends ChunkGenerator
{
	@Override
	public short[][] generateExtBlockSections(World world, Random random, int x, int z, BiomeGrid biomes)
	{
		return new short[world.getMaxHeight() / 16][];
	}
}
