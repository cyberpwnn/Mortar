package mortar.api.rift;

import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.util.HashTreeSet;

import mortar.lang.collection.GList;
import mortar.lang.collection.GMap;
import mortar.util.reflection.V;
import net.minecraft.server.v1_12_R1.NextTickListEntry;
import net.minecraft.server.v1_12_R1.WorldServer;

public class PhysicsThrottle
{
	private Rift rift;
	private World world;
	private int delay;
	private int reschedule;
	private GMap<NextTickListEntry, Integer> delayedEntries;
	private GList<NextTickListEntry> delayedOrder;
	private HashTreeSet<NextTickListEntry> t;

	public PhysicsThrottle(Rift rift)
	{
		this.world = rift.getWorld();
		this.rift = rift;
		delay = 0;
		reschedule = 0;
		delayedEntries = new GMap<>();
		delayedOrder = new GList<>();
		t = getTickList();
	}

	public int getDelay()
	{
		return delay;
	}

	public void setDelay(int delay)
	{
		this.delay = delay;
	}

	public void dumpTicklist()
	{
		try
		{
			GMap<Integer, GList<NextTickListEntry>> v = delayedEntries.flip();
			delayedEntries.clear();
			delayedOrder.clear();

			for(GList<NextTickListEntry> i : v.sortV().reverse())
			{
				t.addAll(i);
			}
		}

		catch(Throwable e)
		{
			e.printStackTrace();
		}
	}

	public void tick()
	{
		if(delay > 0 && reschedule <= 0)
		{
			Iterator<NextTickListEntry> it = t.iterator();

			while(it.hasNext())
			{
				NextTickListEntry e = it.next();
				it.remove();

				if(rift.isWorldBorderEnabled() && world.getWorldBorder().isInside(new Location(world, e.a.getX(), e.a.getY(), e.a.getZ())))
				{
					delayedEntries.put(e, delay);
					delayedOrder.add(e);
				}

				else
				{
					delayedEntries.put(e, delay);
					delayedOrder.add(e);
				}
			}
		}

		if(delayedEntries.size() > 0)
		{
			for(NextTickListEntry i : delayedOrder.copy())
			{
				try
				{
					if(delayedEntries.get(i) <= 0)
					{
						t.add(i);
						delayedEntries.remove(i);
						delayedOrder.remove(i);
					}

					else
					{
						delayedEntries.put(i, delayedEntries.get(i) - 1);
					}
				}

				catch(Throwable e)
				{
					delayedEntries.remove(i);
					delayedOrder.remove(i);
				}
			}
		}

		reschedule--;
	}

	public HashTreeSet<NextTickListEntry> getTickList()
	{
		CraftWorld w = (CraftWorld) world;
		WorldServer ws = new V(w).get("world");
		HashTreeSet<NextTickListEntry> set = new V(ws).get("nextTickList");

		return set;
	}
}
