package mortar.api.fx;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import mortar.api.particle.ParticleEffect;

public class ParticlePortal extends ParticleBase implements SpreadEffect, DirectionalEffect
{
	private double spread;
	private Vector direction;

	public ParticlePortal()
	{
		this.spread = 0;
		direction = new Vector(0, 0, 0);
	}

	@Override
	public ParticlePortal setSpread(double s)
	{
		this.spread = s;
		return this;
	}

	@Override
	public double getSpread()
	{
		return spread;
	}

	@Override
	public void play(Location l, double range)
	{
		ParticleEffect.PORTAL.display(getDirection(), (float) getSpread(), l, range);
	}

	@Override
	public void play(Location l, Player p)
	{
		ParticleEffect.PORTAL.display(getDirection(), (float) getSpread(), l, p);
	}

	@Override
	public ParticlePortal setDirection(Vector v)
	{
		this.direction = v;
		return this;
	}

	@Override
	public Vector getDirection()
	{
		return direction;
	}
}
