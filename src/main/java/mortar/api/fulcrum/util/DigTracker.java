package mortar.api.fulcrum.util;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import mortar.api.fulcrum.ContentAssist;
import mortar.api.nms.Catalyst;
import mortar.api.sched.J;
import mortar.api.world.PE;
import mortar.compute.math.M;
import mortar.lang.collection.GList;
import mortar.lang.collection.GMap;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.PacketPlayOutBlockBreakAnimation;

public class DigTracker
{
	private GMap<Block, Double> speed;
	private GMap<Block, Double> progress;
	private GMap<Player, Block> digging;

	public DigTracker()
	{
		speed = new GMap<>();
		progress = new GMap<>();
		digging = new GMap<>();
		J.sr(() -> tick(), 0);
	}

	public GList<Player> getDigging(Block b)
	{
		GList<Player> p = digging.flip().get(b);

		return p != null ? p : new GList<>();
	}

	private int getBlockPos(Block b)
	{
		return b.getX() + b.getY() + b.getZ();
	}

	public void tick()
	{
		for(Block i : progress.k())
		{
			double digSpeed = speed.containsKey(i) ? speed.get(i) : 1D / 40D;
			progress.put(i, progress.get(i) + digSpeed);
			Player breaker = getDigging(i).get(0);
			if(progress.get(i) >= 1D)
			{
				progress.put(i, 1D);
				Bukkit.getPluginManager().callEvent(new BlockBreakEvent(i, breaker));
				progress.remove(i);
			}

			else
			{
				if(M.interval(4))
				{
					ContentAssist.getBlock(i).playSound(i, BlockSoundCategory.DIG);
				}
			}

			if(progress.containsKey(i))
			{
				if(!i.getType().equals(Material.GLASS) && !i.isEmpty())
				{
					i.setType(Material.GLASS);
				}

				Catalyst.host.sendViewDistancedPacket(i.getChunk(), new PacketPlayOutBlockBreakAnimation(getBlockPos(i), new BlockPosition(i.getX(), i.getY(), i.getZ()), (int) (progress.get(i) * 9.0)));
			}

			else
			{
				if(i.getType().equals(Material.GLASS))
				{
					i.setType(Material.BARRIER);
				}

				Catalyst.host.sendViewDistancedPacket(i.getChunk(), new PacketPlayOutBlockBreakAnimation(getBlockPos(i), new BlockPosition(i.getX(), i.getY(), i.getZ()), (int) (-1)));
			}
		}

		if(progress.isEmpty())
		{
			digging.clear();
			speed.clear();
		}

		for(Player i : digging.k())
		{
			PE.SLOW_DIGGING.a(-1).d(3).apply(i);
		}
	}

	public void finishedDigging(Player p, Block b)
	{
		if(p.getGameMode().equals(GameMode.CREATIVE))
		{
			return;
		}

		cancelledDigging(p, b);
	}

	public void cancelledDigging(Player p, Block b)
	{
		if(p.getGameMode().equals(GameMode.CREATIVE))
		{
			return;
		}

		J.s(() ->
		{
			digging.remove(p);

			if(getDigging(b).isEmpty())
			{
				progress.remove(b);
				speed.remove(b);
			}

			if(b.getType().equals(Material.GLASS))
			{
				b.setType(Material.BARRIER);
			}

			Catalyst.host.sendViewDistancedPacket(b.getChunk(), new PacketPlayOutBlockBreakAnimation(getBlockPos(b), new BlockPosition(b.getX(), b.getY(), b.getZ()), (int) (-1)));
		});
	}

	public void startedDigging(Player p, Block b, double speed)
	{
		if(p.getGameMode().equals(GameMode.CREATIVE))
		{
			return;
		}

		J.s(() ->
		{
			if(isDigging(p))
			{
				cancelledDigging(p, getDigging(p));
			}

			digging.put(p, b);
			progress.put(b, 0D);
			this.speed.put(b, speed);
		});
	}

	public boolean isDigging(Player p)
	{
		return digging.containsKey(p);
	}

	public Block getDigging(Player p)
	{
		return digging.get(p);
	}
}
