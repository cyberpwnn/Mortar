package mortar.api.fx;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import mortar.api.particle.ParticleEffect;

public class ParticleWaterSplash extends ParticleBase implements DirectionalEffect
{
	private Vector direction;

	public ParticleWaterSplash()
	{
		direction = new Vector();
	}

	@Override
	public ParticleWaterSplash setDirection(Vector v)
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
		ParticleEffect.WATER_SPLASH.display(getDirection(), 0f, l, range);
	}

	@Override
	public void play(Location l, Player p)
	{
		ParticleEffect.WATER_SPLASH.display(getDirection(), 0f, l, p);
	}
}
