package mortar.api.fx;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import mortar.api.particle.ParticleEffect;

public class ParticleBarrier extends ParticleBase
{
	@Override
	public void play(Location l, double range)
	{
		ParticleEffect.BARRIER.display(0f, 1, l, range);
	}

	@Override
	public void play(Location l, Player p)
	{
		ParticleEffect.BARRIER.display(0f, 1, l, p);
	}
}