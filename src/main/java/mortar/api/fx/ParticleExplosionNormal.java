package mortar.api.fx;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import mortar.api.particle.ParticleEffect;

public class ParticleExplosionNormal extends ParticleBase implements MotionEffect
{
	private double speed;

	public ParticleExplosionNormal()
	{
		this.speed = 0;
	}

	@Override
	public ParticleExplosionNormal setSpeed(double s)
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
		ParticleEffect.EXPLOSION_NORMAL.display((float) getSpeed(), 1, l, range);
	}

	@Override
	public void play(Location l, Player p)
	{
		ParticleEffect.EXPLOSION_NORMAL.display((float) getSpeed(), 1, l, p);
	}
}
