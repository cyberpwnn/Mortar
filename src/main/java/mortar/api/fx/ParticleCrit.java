package mortar.api.fx;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import mortar.api.particle.ParticleEffect;

public class ParticleCrit extends ParticleBase implements MotionEffect, DirectionalEffect
{
	private double speed;
	private Vector direction;
	private boolean magical;

	public ParticleCrit()
	{
		magical = false;
		speed = 0;
		direction = new Vector();
	}

	public ParticleCrit setMagical(boolean m)
	{
		this.magical = m;
		return this;
	}

	public boolean isMagical()
	{
		return magical;
	}

	@Override
	public ParticleCrit setSpeed(double s)
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
	public ParticleCrit setDirection(Vector v)
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
		(isMagical() ? ParticleEffect.CRIT_MAGIC : ParticleEffect.CRIT).display(getDirection(), (float) getSpeed(), l, range);
	}

	@Override
	public void play(Location l, Player p)
	{
		(isMagical() ? ParticleEffect.CRIT_MAGIC : ParticleEffect.CRIT).display(getDirection(), (float) getSpeed(), l, p);
	}
}
