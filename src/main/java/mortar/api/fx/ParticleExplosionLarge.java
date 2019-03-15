package mortar.api.fx;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import mortar.api.particle.ParticleEffect;

public class ParticleExplosionLarge extends ParticleBase implements SizedEffect
{
	private double speed;

	public ParticleExplosionLarge()
	{
		this.speed = 0;
	}

	@Override
	public ParticleExplosionLarge setSize(double s)
	{
		this.speed = s;
		return this;
	}

	@Override
	public double getSize()
	{
		return speed;
	}

	@Override
	public void play(Location l, double range)
	{
		ParticleEffect.EXPLOSION_LARGE.display((float) getSize(), 1, l, range);
	}

	@Override
	public void play(Location l, Player p)
	{
		ParticleEffect.EXPLOSION_LARGE.display((float) getSize(), 1, l, p);
	}
}
