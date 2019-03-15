package mortar.scm;

import org.bukkit.Material;
import org.bukkit.util.Vector;

import mortar.api.world.BlockType;

public class ST
{
	public static void makePlateXZ(Vector center, VariableSchematic schematic, Integer radius, BlockType mb)
	{
		for(int i = -radius; i < radius + 1; i++)
		{
			for(int j = -radius; j < radius + 1; j++)
			{
				schematic.add(center.clone().add(new Vector(i, 0, j)), mb);
			}
		}
	}

	public static void makePlateYZ(Vector center, VariableSchematic schematic, Integer radius, BlockType mb)
	{
		for(int i = -radius; i < radius + 1; i++)
		{
			for(int j = -radius; j < radius + 1; j++)
			{
				schematic.add(center.clone().add(new Vector(0, i, j)), mb);
			}
		}
	}

	public static void makePlateXY(Vector center, VariableSchematic schematic, Integer radius, BlockType mb)
	{
		for(int i = -radius; i < radius + 1; i++)
		{
			for(int j = -radius; j < radius + 1; j++)
			{
				schematic.add(center.clone().add(new Vector(i, j, 0)), mb);
			}
		}
	}

	public static void makePlateXZ(Vector center, VariableSchematic schematic, Integer radius, VariableBlock mb)
	{
		for(int i = -radius; i < radius + 1; i++)
		{
			for(int j = -radius; j < radius + 1; j++)
			{
				schematic.add(center.clone().add(new Vector(i, 0, j)), mb);
			}
		}
	}

	public static void makePlateYZ(Vector center, VariableSchematic schematic, Integer radius, VariableBlock mb)
	{
		for(int i = -radius; i < radius + 1; i++)
		{
			for(int j = -radius; j < radius + 1; j++)
			{
				schematic.add(center.clone().add(new Vector(0, i, j)), mb);
			}
		}
	}

	public static void makePlateXY(Vector center, VariableSchematic schematic, Integer radius, VariableBlock mb)
	{
		for(int i = -radius; i < radius + 1; i++)
		{
			for(int j = -radius; j < radius + 1; j++)
			{
				schematic.add(center.clone().add(new Vector(i, j, 0)), mb);
			}
		}
	}

	public static void clearPlateXZ(Vector center, VariableSchematic schematic, Integer radius)
	{
		for(int i = -radius; i < radius + 1; i++)
		{
			for(int j = -radius; j < radius + 1; j++)
			{
				schematic.remove(center.clone().add(new Vector(i, 0, j)));
			}
		}
	}

	public static void clearPlateXY(Vector center, VariableSchematic schematic, Integer radius)
	{
		for(int i = -radius; i < radius + 1; i++)
		{
			for(int j = -radius; j < radius + 1; j++)
			{
				schematic.remove(center.clone().add(new Vector(i, j, 0)));
			}
		}
	}

	public static void clearPlateYZ(Vector center, VariableSchematic schematic, Integer radius)
	{
		for(int i = -radius; i < radius + 1; i++)
		{
			for(int j = -radius; j < radius + 1; j++)
			{
				schematic.remove(center.clone().add(new Vector(0, i, j)));
			}
		}
	}

	public static void clear(Vector v, VariableSchematic schematic)
	{
		schematic.remove(v);
	}

	public static VariableBlock wildcard(Material material, int size)
	{
		VariableBlock vb = new VariableBlock(Material.AIR);
		vb.getBlocks().clear();

		for(int i = 0; i < size; i++)
		{
			vb.addBlock(new BlockType(material, (byte) i));
		}

		return vb;
	}
}
