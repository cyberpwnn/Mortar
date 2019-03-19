package mortar.api.fx;

import java.awt.Color;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import mortar.api.nms.NMSVersion;
import mortar.api.nms.PacketBuffer;
import mortar.api.particle.ParticleEffect;
import mortar.api.particle.ParticleEffect.ParticleColor;
import mortar.api.world.Area;
import mortar.util.reflection.V;
import net.minecraft.server.v1_13_R2.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_13_R2.ParticleParam;
import net.minecraft.server.v1_13_R2.ParticleParamRedstone;

public class ParticleRedstone extends ParticleBase implements ColoredEffect
{
	private Color color;

	public ParticleRedstone()
	{
		this.color = Color.WHITE;
	}

	@Override
	public void play(Location l, double range)
	{
		if(NMSVersion.current().equals(NMSVersion.R1_13))
		{
			ParticleParam pp = new ParticleParamRedstone(getColor().getRed(), getColor().getGreen(), getColor().getBlue(), 1f);
			PacketPlayOutWorldParticles px = new PacketPlayOutWorldParticles();
			PacketBuffer b = new PacketBuffer();
			new V(px).set("a", (float) l.getX());
			new V(px).set("b", (float) l.getY());
			new V(px).set("c", (float) l.getZ());
			new V(px).set("d", 0f);
			new V(px).set("e", 0f);
			new V(px).set("f", 0f);
			new V(px).set("g", 0f);
			new V(px).set("h", 1);
			new V(px).set("i", range > 64);
			new V(px).set("j", pp);
			Area a = new Area(l, range);
			b.q(px);

			for(Player i : a.getNearbyPlayers())
			{
				b.flush(i);
			}

			return;
		}

		ParticleColor c = new ParticleEffect.OrdinaryColor(getColor().getRed(), getColor().getGreen(), getColor().getBlue());
		ParticleEffect.REDSTONE.display(c, l, range);
	}

	@Override
	public void play(Location l, Player p)
	{
		if(NMSVersion.current().equals(NMSVersion.R1_13))
		{
			ParticleParam pp = new ParticleParamRedstone(getColor().getRed() / 255f, getColor().getGreen() / 255f, getColor().getBlue() / 255f, 0.75f);
			PacketPlayOutWorldParticles px = new PacketPlayOutWorldParticles();
			PacketBuffer b = new PacketBuffer();
			new V(px).set("a", (float) l.getX());
			new V(px).set("b", (float) l.getY());
			new V(px).set("c", (float) l.getZ());
			new V(px).set("d", 0f);
			new V(px).set("e", 0f);
			new V(px).set("f", 0f);
			new V(px).set("g", 0f);
			new V(px).set("h", 1);
			new V(px).set("i", p.getLocation().distanceSquared(l) > 64 * 64);
			new V(px).set("j", pp);
			b.q(px).flush(p);

			return;
		}

		ParticleColor c = new ParticleEffect.OrdinaryColor(getColor().getRed(), getColor().getGreen(), getColor().getBlue());
		ParticleEffect.REDSTONE.display(c, l, p);
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
}
