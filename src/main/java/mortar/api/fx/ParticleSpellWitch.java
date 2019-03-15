package mortar.api.fx;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import mortar.api.particle.ParticleEffect;

public class ParticleSpellWitch extends ParticleBase implements MotionEffect
{
	private double speed;

	public ParticleSpellWitch()
	{
		this.speed = 0;
	}

	@Override
	public ParticleSpellWitch setSpeed(double s)
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
		ParticleEffect.SPELL_WITCH.display((float) getSpeed(), 1, l, range);
	}

	@Override
	public void play(Location l, Player p)
	{
		ParticleEffect.SPELL_WITCH.display((float) getSpeed(), 1, l, p);
	}
}
