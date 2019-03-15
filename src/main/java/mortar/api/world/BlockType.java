package mortar.api.world;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import mortar.bukkit.plugin.GhostWorldController;
import mortar.bukkit.plugin.Mortar;

public class BlockType
{
	private Material material;
	private Byte data;
	private Material cmat = null;
	private short cdur = -1;

	@SuppressWarnings("deprecation")
	public BlockType(String s)
	{
		Material material = null;
		String m = "0";
		String b = "0";

		if(s.contains(":"))
		{
			m = s.split(":")[0];
			b = s.split(":")[1];
		}

		else
		{
			m = s;
		}

		try
		{
			material = Material.getMaterial(Integer.valueOf(m));

			if(material == null)
			{
				try
				{
					material = Material.valueOf(m.toUpperCase());

					if(material == null)
					{

					}
				}

				catch(Exception e)
				{

				}
			}
		}

		catch(Exception e)
		{
			try
			{
				material = Material.valueOf(m.toUpperCase());

				if(material == null)
				{

				}
			}

			catch(Exception ex)
			{

			}
		}

		try
		{
			data = Integer.valueOf(b).byteValue();
		}

		catch(Exception e)
		{
			data = (byte) 0;
		}
	}

	public BlockType(Material material, Byte data)
	{
		this.material = material;
		this.data = data;
	}

	public BlockType(Material material)
	{
		this.material = material;
		data = 0;
	}

	public BlockType(Material material, short s, boolean spawner)
	{
		this.material = Material.MOB_SPAWNER;
		data = 0;
		cmat = material;
		cdur = s;
	}

	public BlockType(Location location)
	{
		this(location.getBlock());
	}

	public static BlockType snapshotOf(Location l)
	{
		return Mortar.getController(GhostWorldController.class).get(l);
	}

	@SuppressWarnings("deprecation")
	public BlockType(BlockState state)
	{
		material = state.getType();
		data = state.getData().getData();
	}

	@SuppressWarnings("deprecation")
	public BlockType(Block block)
	{
		material = block.getType();
		data = block.getData();
	}

	public BlockType()
	{
		material = Material.AIR;
		data = 0;
	}

	public Material getMaterial()
	{
		return material;
	}

	public void setMaterial(Material material)
	{
		this.material = material;
	}

	public Byte getData()
	{
		return data;
	}

	public void setData(Byte data)
	{
		this.data = data;
	}

	@Override
	public String toString()
	{
		if(getData() == 0)
		{
			return getMaterial().toString();
		}

		return getMaterial().toString() + ":" + getData();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + cdur;
		result = prime * result + ((cmat == null) ? 0 : cmat.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((material == null) ? 0 : material.hashCode());
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
		BlockType other = (BlockType) obj;
		if(cdur != other.cdur)
		{
			return false;
		}
		if(cmat != other.cmat)
		{
			return false;
		}
		if(data == null)
		{
			if(other.data != null)
			{
				return false;
			}
		}
		else if(!data.equals(other.data))
		{
			return false;
		}
		if(material != other.material)
		{
			return false;
		}
		return true;
	}
}
