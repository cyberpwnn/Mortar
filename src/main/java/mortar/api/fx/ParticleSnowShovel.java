package mortar.api.fx;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import mortar.api.particle.ParticleEffect;

public class ParticleSnowShovel extends ParticleBase implements MotionEffect, DirectionalEffect
{
	private double speed;
	private Vector direction;

	public ParticleSnowShovel()
	{
		speed = 0;
		direction = new Vector();
	}

	@Override
	public ParticleSnowShovel setSpeed(double s)
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
	public ParticleSnowShovel setDirection(Vector v)
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
	public void play(Location l, double range)
	{
		ParticleEffect.SNOW_SHOVEL.display(getDirection(), (float) getSpeed(), l, range);
	}

	@Override
	public void play(Location l, Player p)
	{
		ParticleEffect.SNOW_SHOVEL.display(getDirection(), (float) getSpeed(), l, p);
	}
}
