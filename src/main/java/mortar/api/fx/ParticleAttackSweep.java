package mortar.api.fx;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import mortar.api.particle.ParticleEffect;

public class ParticleAttackSweep extends ParticleBase implements SizedEffect
{
	private double size;

	public ParticleAttackSweep()
	{
		size = 1f;
	}

	@Override
	public void play(Location l, double range)
	{
		ParticleEffect.SWEEP_ATTACK.display((float) getSize(), 1, l, range);
	}

	@Override
	public void play(Location l, Player p)
	{
		ParticleEffect.SWEEP_ATTACK.display((float) getSize(), 1, l, p);
	}

	@Override
	public ParticleAttackSweep setSize(double s)
	{
		this.size = s;
		return this;
	}

	@Override
	public double getSize()
	{
		return size;
	}
}