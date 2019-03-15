package mortar.api.fx;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import mortar.api.particle.ParticleEffect;

public class ParticleSmoke extends ParticleBase implements MotionEffect, DirectionalEffect
{
	private double speed;
	private Vector direction;
	private boolean huge;

	public ParticleSmoke()
	{
		huge = false;
		speed = 0;
		direction = new Vector();
	}

	public ParticleSmoke setHuge(boolean m)
	{
		this.huge = m;
		return this;
	}

	public boolean isHuge()
	{
		return huge;
	}

	@Override
	public ParticleSmoke setSpeed(double s)
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
	public ParticleSmoke setDirection(Vector v)
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
		(isHuge() ? ParticleEffect.SMOKE_LARGE : ParticleEffect.SMOKE_NORMAL).display(getDirection(), (float) getSpeed(), l, range);
	}

	@Override
	public void play(Location l, Player p)
	{
		(isHuge() ? ParticleEffect.SMOKE_LARGE : ParticleEffect.SMOKE_NORMAL).display(getDirection(), (float) getSpeed(), l, p);
	}
}
