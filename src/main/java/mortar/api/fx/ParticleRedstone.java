package mortar.api.fx;

import java.awt.Color;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import mortar.api.nms.NMP;

public class ParticleRedstone extends ParticleBase implements ColoredEffect
{
	private Color color;
	private float size;

	public ParticleRedstone()
	{
		this.color = Color.WHITE;
		size = 1f;
	}

	@Override
	public void play(Location l, double range)
	{
		NMP.host.redstoneParticle(range, getColor(), l, 1f);
	}

	@Override
	public void play(Location l, Player p)
	{
		NMP.host.redstoneParticle(p, getColor(), l, 1f);
	}

	@Override
	public ParticleRedstone setColor(Color color)
	{
		this.color = color;
		return this;
	}

	@Override
	public Color getColor()
	{
		return color;
	}

	public ParticleRedstone setSize(float size)
	{
		this.size = size;
		return this;
	}

	public float getSize()
	{
		return size;
	}
}
