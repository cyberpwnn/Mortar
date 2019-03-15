package mortar.api.fx;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import mortar.api.particle.ParticleEffect;

public class ParticleFireworkSpark extends ParticleBase implements MotionEffect, DirectionalEffect
{
	private double speed;
	private Vector direction;

	public ParticleFireworkSpark()
	{
		this.speed = 0;
		direction = new Vector();
	}

	@Override
	public ParticleFireworkSpark setSpeed(double s)
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
	public ParticleFireworkSpark setDirection(Vector v)
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
		ParticleEffect.FIREWORKS_SPARK.display(getDirection(), (float) getSpeed(), l, range);
	}

	@Override
	public void play(Location l, Player p)
	{
		ParticleEffect.FIREWORKS_SPARK.display(getDirection(), (float) getSpeed(), l, p);
	}
}
