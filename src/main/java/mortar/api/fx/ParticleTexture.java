package mortar.api.fx;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import mortar.api.particle.ParticleEffect;
import mortar.api.world.BlockType;

public class ParticleTexture extends ParticleBase implements MotionEffect, DirectionalEffect, TypeEffect
{
	private Vector direction;
	private BlockType type;
	private double speed;
	private boolean blockDust;

	public ParticleTexture()
	{
		direction = new Vector();
		type = new BlockType(Material.STONE);
		speed = 0f;
		blockDust = false;
	}

	public ParticleTexture setBlockDust(boolean dusty)
	{
		blockDust = dusty;
		return this;
	}

	public boolean isBlockDust()
	{
		return blockDust;
	}

	@Override
	public ParticleTexture setDirection(Vector v)
	{
		this.direction = v;
		return this;
	}

	@Override
	public Vector getDirection()
	{
		return direction;
	}

	@Override
	public ParticleTexture setType(BlockType type)
	{
		this.type = type;
		return this;
	}

	@Override
	public BlockType getType()
	{
		return type;
	}

	@Override
	public ParticleTexture setSpeed(double s)
	{
		this.speed = s;
		return this;
	}

	@Override
	public double getSpeed()
	{
		return speed;
	}

	@Override
	public void play(Location l, double range)
	{
		if(getType().getMaterial().isItem())
		{
			ParticleEffect.ItemData d = new ParticleEffect.ItemData(getType().getMaterial(), getType().getData());
			ParticleEffect.ITEM_CRACK.display(d, getDirection(), (float) getSpeed(), l, range);
		}

		if(getType().getMaterial().isBlock())
		{
			ParticleEffect.BlockData d = new ParticleEffect.BlockData(getType().getMaterial(), getType().getData());
			(isBlockDust() ? ParticleEffect.BLOCK_DUST : ParticleEffect.BLOCK_CRACK).display(d, getDirection(), (float) getSpeed(), l, range);
		}
	}

	@Override
	public void play(Location l, Player p)
	{
		if(getType().getMaterial().isItem())
		{
			ParticleEffect.ItemData d = new ParticleEffect.ItemData(getType().getMaterial(), getType().getData());
			ParticleEffect.ITEM_CRACK.display(d, getDirection(), (float) getSpeed(), l, p);
		}

		if(getType().getMaterial().isBlock())
		{
			ParticleEffect.BlockData d = new ParticleEffect.BlockData(getType().getMaterial(), getType().getData());
			(isBlockDust() ? ParticleEffect.BLOCK_DUST : ParticleEffect.BLOCK_CRACK).display(d, getDirection(), (float) getSpeed(), l, p);
		}
	}
}
