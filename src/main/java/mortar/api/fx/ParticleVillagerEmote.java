package mortar.api.fx;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import mortar.api.particle.ParticleEffect;

public class ParticleVillagerEmote extends ParticleBase
{
	private boolean angry;

	public ParticleVillagerEmote()
	{
		angry = false;
	}

	public ParticleVillagerEmote setAngry(boolean d)
	{
		this.angry = d;
		return this;
	}

	public boolean isAngry()
	{
		return angry;
	}

	@Override
	public void play(Location l, double range)
	{
		(isAngry() ? ParticleEffect.VILLAGER_ANGRY : ParticleEffect.VILLAGER_HAPPY).display(0f, 1, l, range);
	}

	@Override
	public void play(Location l, Player p)
	{
		(isAngry() ? ParticleEffect.VILLAGER_ANGRY : ParticleEffect.VILLAGER_HAPPY).display(0f, 1, l, p);
	}
}