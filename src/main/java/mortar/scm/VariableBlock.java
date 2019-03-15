package mortar.scm;

import org.bukkit.Material;

import mortar.api.world.BlockType;
import mortar.lang.collection.GList;

/**
 * Represents a block that can be multiple blocks (used for variable schematics)
 *
 * @author cyberpwn
 */
public class VariableBlock
{
	private GList<BlockType> blocks;

	public VariableBlock()
	{
		blocks = new GList<BlockType>();
	}

	public VariableBlock(String data)
	{
		this();

		fromString(data);
	}

	/**
	 * Create a variable block
	 *
	 * @param blocks
	 *            blocks
	 */
	public VariableBlock(BlockType... blocks)
	{
		this.blocks = new GList<BlockType>(blocks);
	}

	/**
	 * Add a materialblock into this variable block
	 *
	 * @param material
	 *            the material
	 * @param data
	 *            the data
	 */
	public VariableBlock(Material material, Byte data)
	{
		this(new BlockType(material, data));
	}

	/**
	 * Add a materialblock into this variable block
	 *
	 * @param material
	 *            the material
	 */
	public VariableBlock(Material material)
	{
		this(new BlockType(material));
	}

	/**
	 * Get all variable blocks
	 *
	 * @return the variable blocks
	 */
	public GList<BlockType> getBlocks()
	{
		return blocks;
	}

	public BlockType random()
	{
		return blocks.pickRandom();
	}

	/**
	 * Is the given block this block? Does the variable block contain this block
	 *
	 * @param block
	 *            the given block
	 * @return true if it is
	 */
	public boolean is(BlockType block)
	{
		return blocks.contains(block);
	}

	/**
	 * Add a block to the list
	 *
	 * @param block
	 *            the block
	 */
	public void addBlock(BlockType block)
	{
		if(!blocks.contains(block))
		{
			blocks.add(block);
		}
	}

	/**
	 * Remove a block from the list
	 *
	 * @param block
	 *            the block
	 */
	public void removeBlock(BlockType block)
	{
		blocks.remove(block);
	}

	public void fromString(String string)
	{
		blocks.clear();

		if(string.contains(","))
		{
			for(String i : string.split(","))
			{
				try
				{
					blocks.add(new BlockType(i));
				}

				catch(Exception e)
				{

				}
			}
		}

		else
		{
			try
			{
				blocks.add(new BlockType(string));
			}

			catch(Exception e)
			{

			}
		}
	}

	@Override
	public String toString()
	{
		GList<String> s = new GList<String>();

		for(BlockType i : blocks)
		{
			s.add(i.toString());
		}

		return s.toString(",");
	}
}
