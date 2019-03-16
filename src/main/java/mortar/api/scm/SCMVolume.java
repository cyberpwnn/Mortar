package mortar.api.scm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.bukkit.Location;
import org.bukkit.Material;

import mortar.api.world.BlockType;
import mortar.api.world.Cuboid;
import mortar.api.world.Dimension;

public class SCMVolume implements IVolume
{
	private VariableSchematic variableSchem;
	private VectorSchematic vectorSchem;
	private Dimension dimension;
	private PermutationType type;

	public SCMVolume(Dimension dimension, PermutationType type)
	{
		this.type = type;
		this.dimension = dimension;
		variableSchem = new VariableSchematic(dimension);
		compile();
	}

	public SCMVolume(File file) throws IOException
	{
		load(file);
		compile();
	}

	public SCMVolume(Cuboid c, PermutationType type) throws IOException
	{
		this.dimension = new Dimension(c.getSizeX(), c.getSizeY(), c.getSizeZ());
		this.type = type;
		variableSchem = new VariableSchematic(c);
		compile();
	}

	@Override
	public void compile()
	{
		vectorSchem = variableSchem.toVectorSchematic();
		setPermutationType(type);
	}

	@Override
	public PermutationType getPermutationType()
	{
		return type;
	}

	@Override
	public void setPermutationType(PermutationType type)
	{
		vectorSchem.setPermutationType(type);
	}

	@Override
	public void save(File f) throws IOException
	{
		f.getParentFile().mkdirs();
		FileOutputStream fos = new FileOutputStream(f);
		GZIPOutputStream gzo = new GZIPOutputStream(fos);
		DataOutputStream dos = new DataOutputStream(gzo);
		dos.writeInt(dimension.getWidth());
		dos.writeInt(dimension.getHeight());
		dos.writeInt(dimension.getDepth());
		dos.writeByte(getPermutationType().ordinal());

		for(int i = 0; i < dimension.getWidth(); i++)
		{
			for(int j = 0; j < dimension.getHeight(); j++)
			{
				for(int k = 0; k < dimension.getDepth(); k++)
				{
					dos.writeInt(getVariableSchematic().getSchematic()[i][j][k].getBlocks().size());

					for(BlockType l : getVariableSchematic().getSchematic()[i][j][k].getBlocks())
					{
						dos.writeUTF(l.getMaterial().name());
						dos.writeByte(l.getData());
					}
				}
			}
		}

		dos.close();
	}

	@Override
	public void load(File f) throws IOException
	{
		FileInputStream fin = new FileInputStream(f);
		GZIPInputStream gzi = new GZIPInputStream(fin);
		DataInputStream din = new DataInputStream(gzi);
		this.dimension = new Dimension(din.readInt(), din.readInt(), din.readInt());
		this.variableSchem = new VariableSchematic(dimension);
		this.type = PermutationType.values()[(int) din.readByte()];

		for(int i = 0; i < dimension.getWidth(); i++)
		{
			for(int j = 0; j < dimension.getHeight(); j++)
			{
				for(int k = 0; k < dimension.getDepth(); k++)
				{
					int size = din.readInt();
					VariableBlock vb = new VariableBlock();
					for(int l = 0; l < size; l++)
					{
						vb.addBlock(new BlockType(Material.valueOf(din.readUTF()), din.readByte()));
					}

					variableSchem.getSchematic()[i][j][k] = vb;
				}
			}
		}

		din.close();
	}

	@Override
	public IMappedVolume match(Location location)
	{
		return vectorSchem.match(location);
	}

	@Override
	public VariableSchematic getVariableSchematic()
	{
		return variableSchem;
	}

	@Override
	public VectorSchematic getVectorSchematic()
	{
		return vectorSchem;
	}

	@Override
	public Dimension getDimension()
	{
		return dimension;
	}

	@Override
	public void place(Location origin)
	{
		getVariableSchematic().place(origin);
	}
}
