package mortar.api.scm;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import mortar.lang.collection.GMap;

public class MappedSCMVolume implements IMappedVolume
{
	private GMap<Vector, Location> mapping;
	private GMap<Location, Vector> reverseMapping;
	private VectorSchematic schematic;

	public MappedSCMVolume(VectorSchematic schematic, GMap<Vector, Location> mapping)
	{
		this.schematic = schematic;
		this.mapping = mapping;
		reverseMapping = new GMap<Location, Vector>();

		for(Vector i : mapping.k())
		{
			reverseMapping.put(mapping.get(i), i);
		}
	}

	@Override
	public VariableBlock getType(Vector v)
	{
		return schematic.getSchematic().get(v);
	}

	@Override
	public VariableBlock getType(Location l)
	{
		return getType(reverseMapping.get(l));
	}

	@Override
	public GMap<Vector, VariableBlock> getMapping()
	{
		return schematic.getSchematic().copy();
	}

	public VectorSchematic getSchematic()
	{
		return schematic;
	}

	@Override
	public GMap<Vector, Location> getRealizedMapping()
	{
		return mapping.copy();
	}

	@Override
	public GMap<Location, Vector> getReverseRealizedMapping()
	{
		return reverseMapping.copy();
	}
}
