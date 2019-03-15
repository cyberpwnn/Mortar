package mortar.scm;

import org.bukkit.Location;
import org.bukkit.material.Stairs;
import org.bukkit.util.Vector;

import mortar.api.world.Axis;
import mortar.api.world.BlockType;
import mortar.api.world.Direction;
import mortar.api.world.VectorMath;
import mortar.lang.collection.GList;
import mortar.lang.collection.GMap;

/**
 * Vector schematics
 *
 * @author cyberpwn
 */
public class VectorSchematic
{
	private final GMap<Vector, VariableBlock> schematic;
	private PermutationType pt;
	private GList<VectorSchematic> types;

	/**
	 * Create a vector schematic
	 */
	public VectorSchematic()
	{
		schematic = new GMap<Vector, VariableBlock>();
		pt = PermutationType.NONE;
		types = new GList<VectorSchematic>();
	}

	public void setPermutationType(PermutationType type)
	{
		types.clear();

		if(type.equals(PermutationType.NONE))
		{
			return;
		}

		if(type.equals(PermutationType.VERTICAL_AXIS))
		{
			types.add(rotate(Direction.N, Direction.N));
			types.add(rotate(Direction.N, Direction.E));
			types.add(rotate(Direction.N, Direction.W));
			types.add(rotate(Direction.N, Direction.S));
		}

		if(type.equals(PermutationType.ANY_AXIS))
		{
			for(Direction i : Direction.udnews())
			{
				for(Direction j : Direction.udnews())
				{
					for(Direction k : Direction.udnews())
					{
						types.add(rotate(i, j));
						types.add(rotate(i, j).rotate(j, k));
					}
				}
			}
		}

		for(VectorSchematic c : types.copy())
		{
			types.add(c.flip(Axis.X));
			types.add(c.flip(Axis.Y));
			types.add(c.flip(Axis.Z));
		}

		types.add(flip(Axis.X));
		types.add(flip(Axis.Y));
		types.add(flip(Axis.Z));

		types.removeDuplicates();

		this.pt = type;
	}

	public PermutationType getPermutationType()
	{
		return pt;
	}

	/**
	 * Does the vector schematic's variable blocks contain the given material block?
	 *
	 * @param mb
	 *            the material block
	 * @return true if it does
	 */
	public boolean contains(BlockType mb)
	{
		for(Vector i : schematic.k())
		{
			if(schematic.get(i).is(mb))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Does this vector schematic contain multiple of the given material blocks
	 *
	 * @param mb
	 *            the materialblock
	 * @return true if it does
	 */
	public boolean containsMultiple(BlockType mb)
	{
		return find(mb).size() > 1;
	}

	/**
	 * Find all vector references that match the given material block
	 *
	 * @param mb
	 *            the materialblock
	 * @return the vector matches
	 */
	public GList<Vector> find(BlockType mb)
	{
		GList<Vector> vectors = new GList<Vector>();

		for(Vector i : schematic.k())
		{
			if(schematic.get(i).is(mb))
			{
				vectors.add(i);
			}
		}

		return vectors;
	}

	/**
	 * Match the location as part of a multiblock structure
	 *
	 * @param location
	 *            the location
	 * @return the mapping or null if no match
	 */
	public IMappedVolume match(Location location)
	{
		if(pt.equals(PermutationType.NONE))
		{
			BlockType mb = BlockType.snapshotOf(location);

			for(Vector i : find(mb))
			{
				Vector shift = i;
				Location base = location.clone().subtract(shift);
				GMap<Vector, Location> map = new GMap<Vector, Location>();
				Boolean found = true;

				for(Vector j : schematic.k())
				{
					Location attempt = base.clone().add(j);
					BlockType mbx = BlockType.snapshotOf(attempt);

					if(schematic.get(j).is(mbx))
					{
						map.put(j, attempt);
					}

					else
					{
						found = false;
						break;
					}
				}

				if(found)
				{
					return new MappedSCMVolume(this, map);
				}
			}

			return null;
		}

		else
		{
			for(VectorSchematic i : types)
			{
				IMappedVolume vol = i.match(location);

				if(vol != null)
				{
					return vol;
				}
			}
		}

		return null;
	}

	public Vector getNormal(GList<Vector> vectorizedList)
	{
		int minx = Integer.MAX_VALUE;
		int miny = Integer.MAX_VALUE;
		int minz = Integer.MAX_VALUE;

		for(Vector i : vectorizedList)
		{
			if(i.getBlockX() < minx)
			{
				minx = i.getBlockX();
			}

			if(i.getBlockY() < miny)
			{
				miny = i.getBlockY();
			}

			if(i.getBlockZ() < minz)
			{
				minz = i.getBlockZ();
			}
		}

		return new Vector(minx < 0 ? -minx : 0, miny < 0 ? -miny : 0, minz < 0 ? -minz : 0);
	}

	@SuppressWarnings("deprecation")
	public VectorSchematic rotate(Direction from, Direction to)
	{
		GMap<Vector, VariableBlock> a = getSchematic().copy();
		GMap<Vector, VariableBlock> b = new GMap<Vector, VariableBlock>();
		VectorSchematic v = new VectorSchematic();
		v.setPermutationType(PermutationType.NONE);

		for(Vector i : a.k())
		{
			VariableBlock mask = new VariableBlock();

			for(BlockType j : a.get(i).getBlocks())
			{
				BlockType k = j;

				if(j.getMaterial().name().contains("_STAIRS"))
				{
					Stairs s = new Stairs(j.getMaterial(), j.getData());

					Direction ax = Direction.fromFace(s.getFacing());
					Direction bx = s.isInverted() ? Direction.U : Direction.D;

					Direction nax = Direction.getDirection(from.angle(ax.toVector(), to));
					Direction nbx = Direction.getDirection(from.angle(bx.toVector(), to));

					if(nax.isVertical() && !nbx.isVertical())
					{
						Direction ncx = nax;
						nax = nbx;
						nbx = ncx;
					}

					if(!nax.isVertical() && nbx.isVertical())
					{
						s.setFacingDirection(nax.blockFace());
						s.setInverted(nbx.equals(Direction.U));
					}

					k = new BlockType(s.getItemType(), s.getData());
				}

				mask.addBlock(k);
			}

			b.put(from.angle(i, to), mask);
		}

		Vector c = getNormal(b.k());

		for(Vector i : b.k())
		{
			Vector d = i.clone().add(c);
			v.getSchematic().put(d, b.get(i));
		}

		return v;
	}

	@SuppressWarnings("deprecation")
	public VectorSchematic flip(Axis axis)
	{
		GMap<Vector, VariableBlock> a = getSchematic().copy();
		GMap<Vector, VariableBlock> b = new GMap<Vector, VariableBlock>();
		VectorSchematic v = new VectorSchematic();
		v.setPermutationType(PermutationType.NONE);

		for(Vector i : a.k())
		{
			VariableBlock mask = new VariableBlock();

			for(BlockType j : a.get(i).getBlocks())
			{
				BlockType k = j;

				if(j.getMaterial().name().contains("_STAIRS"))
				{
					Stairs s = new Stairs(j.getMaterial(), j.getData());
					Direction da = Direction.fromFace(s.getFacing());

					if(Direction.getDirection(axis.negative()).equals(da))
					{
						da = Direction.getDirection(axis.positive());
					}

					if(Direction.getDirection(axis.positive()).equals(da))
					{
						da = Direction.getDirection(axis.negative());
					}

					if(axis.equals(Axis.Y))
					{
						s.setInverted(!s.isInverted());
					}

					s.setFacingDirection(da.blockFace());

					k = new BlockType(s.getItemType(), s.getData());
				}

				mask.addBlock(k);
			}

			b.put(VectorMath.flip(i, axis), mask);
		}

		Vector c = getNormal(b.k());

		for(Vector i : b.k())
		{
			Vector d = i.clone().add(c);
			v.getSchematic().put(d, b.get(i));
		}

		return v;
	}

	/**
	 * Get the schematic
	 *
	 * @return the schematic
	 */
	public GMap<Vector, VariableBlock> getSchematic()
	{
		return schematic;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pt == null) ? 0 : pt.hashCode());
		result = prime * result + ((schematic == null) ? 0 : schematic.hashCode());
		result = prime * result + ((types == null) ? 0 : types.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
		{
			return true;
		}
		if(obj == null)
		{
			return false;
		}
		if(getClass() != obj.getClass())
		{
			return false;
		}
		VectorSchematic other = (VectorSchematic) obj;
		if(pt != other.pt)
		{
			return false;
		}
		if(schematic == null)
		{
			if(other.schematic != null)
			{
				return false;
			}
		}
		else if(!schematic.equals(other.schematic))
		{
			return false;
		}
		if(types == null)
		{
			if(other.types != null)
			{
				return false;
			}
		}
		else if(!types.equals(other.types))
		{
			return false;
		}
		return true;
	}

	/**
	 * Clear the schematic
	 */
	public void clear()
	{
		schematic.clear();
	}

	public GList<VectorSchematic> getTypes()
	{
		return types;
	}
}
