package mortar.api.fx;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import mortar.api.particle.ParticleEffect;

public class ParticleWaterWake extends ParticleBase implements MotionEffect, DirectionalEffect
{
	private double speed;
	private Vector direction;

	public ParticleWaterWake()
	{
		this.speed = 0;
		direction = new Vector();
	}

	@Override
	public ParticleWaterWake setSpeed(double s)
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
	public ParticleWaterWake setDirection(Vector v)
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
		ParticleEffect.WATER_WAKE.display(getDirection(), (float) getSpeed(), l, range);
	}

	@Override
	public void play(Location l, Player p)
	{
		ParticleEffect.WATER_WAKE.display(getDirection(), (float) getSpeed(), l, p);
	}
}
