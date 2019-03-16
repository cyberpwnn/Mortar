package mortar.api.nms;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import mortar.api.sched.A;
import mortar.api.sched.S;
import mortar.api.sched.SR;
import mortar.lang.collection.GList;
import mortar.lang.collection.GMap;
import mortar.lang.collection.GSet;

public class ChunkSendQueue
{
	private GList<Chunk> c = new GList<Chunk>();
	private GMap<Chunk, GSet<Integer>> sections;
	private boolean running;
	private SR s;
	private int interval;
	private int volume;

	public ChunkSendQueue(int interval, int volume)
	{
		this.interval = interval;
		this.volume = volume;
		c = new GList<Chunk>();
		sections = new GMap<Chunk, GSet<Integer>>();
		running = false;
	}

	public boolean hasStuff()
	{
		return c.size() > 0 || sections.size() > 0;
	}

	public void start()
	{
		s = new SR(interval)
		{
			@Override
			public void run()
			{
				if(!sections.isEmpty())
				{
					int l = volume;

					while(l > 0 && !sections.isEmpty())
					{
						Chunk c = sections.k().pop();
						GSet<Integer> s = sections.get(c);

						if(s.isEmpty())
						{
							sections.remove(c);
							continue;
						}

						ShadowChunk sc = NMP.CHUNK.shadow(c);
						for(int i : s)
						{
							sc.modifySection(i);
						}

						PacketBuffer pb = new PacketBuffer().q(sc.flush());

						for(Player i : NMP.CHUNK.nearbyPlayers(c))
						{
							pb.flush(i);
						}

						sections.remove(c);
						l--;
					}
				}

				if(c.isEmpty() || running)
				{
					return;
				}

				int l = volume;
				GList<Chunk> tosend = new GList<Chunk>();

				while(!c.isEmpty() && l > 0)
				{
					l--;
					tosend.add(c.pop());
				}

				running = true;
				new A()
				{
					@Override
					public void run()
					{
						for(Chunk i : tosend)
						{
							new S()
							{
								@Override
								public void run()
								{
									for(Player l : NMP.CHUNK.nearbyPlayers(i))
									{
										NMP.CHUNK.refresh(l, i);
									}
								}
							};
						}

						running = false;
					}
				};
			}
		};
	}

	public void stop()
	{
		s.cancel();
	}

	public boolean isRunning()
	{
		return running;
	}

	public void queue(Chunk c)
	{
		if(!this.c.contains(c))
		{
			this.c.add(c);
		}
	}

	public void queueSection(Chunk c, int section)
	{
		if(!sections.containsKey(c))
		{
			sections.put(c, new GSet<Integer>());
		}

		sections.get(c).add(section);
	}
}
