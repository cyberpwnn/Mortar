package mortar.api.scm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import mortar.api.nms.NMP;
import mortar.api.sched.J;
import mortar.api.world.Cuboid;
import mortar.api.world.MaterialBlock;
import mortar.api.world.VectorMath;
import mortar.compute.math.M;
import mortar.lang.collection.Callback;
import mortar.logic.queue.ChronoLatch;

public class SCMIO
{
	public static SCMProperties sample(File f) throws IOException
	{
		FileInputStream fin = new FileInputStream(f);
		GZIPInputStream gzi = new GZIPInputStream(fin);
		DataInputStream din = new DataInputStream(gzi);
		SCMProperties scm = new SCMProperties(new Vector(din.readInt(), din.readInt(), din.readInt()), new Vector(din.readInt(), din.readInt(), din.readInt()));
		din.close();

		return scm;
	}

	@SuppressWarnings("deprecation")
	public static void read(File f, Location at, Callback<Boolean> done, Callback<Double> percent)
	{
		J.a(() ->
		{
			try
			{
				FileInputStream fin = new FileInputStream(f);
				GZIPInputStream gzi = new GZIPInputStream(fin);
				DataInputStream din = new DataInputStream(gzi);
				SCMProperties scm = new SCMProperties(new Vector(din.readInt(), din.readInt(), din.readInt()), new Vector(din.readInt(), din.readInt(), din.readInt()));
				Location min = at.getBlock().getLocation().subtract(scm.getOrigin());
				Location max = min.clone().add(scm.getSize()).subtract(1, 1, 1);
				Cuboid c = new Cuboid(min, max);
				ChronoLatch latch = new ChronoLatch(500, false);

				J.s(() ->
				{
					for(Chunk i : c.getChunks())
					{
						i.load();
					}

					J.a(() ->
					{
						int of = c.volume();
						int did = 0;
						for(int i = min.getBlockX(); i <= max.getBlockX(); i++)
						{
							for(int k = min.getBlockZ(); k <= max.getBlockZ(); k++)
							{
								for(int j = min.getBlockY(); j <= max.getBlockY(); j++)
								{
									try
									{
										int id = (int) din.readByte();
										byte dat = din.readByte();
										NMP.host.setBlock(new Location(c.getWorld(), i, j, k), new MaterialBlock(Material.getMaterial(id), dat));
									}

									catch(Throwable e1)
									{

									}

									did++;
								}

								if(latch.flip())
								{
									percent.run((double) did / (double) of);
								}
							}
						}

						try
						{
							din.close();
							gzi.close();
							fin.close();
						}

						catch(IOException e1)
						{
							e1.printStackTrace();
						}

						J.s(() -> done.run(true));
						J.s(() ->
						{
							int v = 0;

							for(Chunk i : c.getChunks())
							{
								v++;
								J.s(() ->
								{
									for(Player j : c.getWorld().getPlayers())
									{
										if(NMP.PLAYER.canSee(j, i))
										{
											NMP.CHUNK.refresh(j, i);
										}
									}
								}, M.rand(0, v / 10));
							}
						});
					});
				});
			}

			catch(Throwable e)
			{
				done.run(false);
				e.printStackTrace();
			}
		});
	}

	@SuppressWarnings("deprecation")
	public static void write(File f, Location at, Cuboid c, Callback<Boolean> done, Callback<Double> percent)
	{
		GhostWorld gw = new GhostWorld();
		Location min = c.getLowerNE();
		Location max = c.getUpperSW();
		Vector origin = VectorMath.directionNoNormal(min, at);

		J.s(() ->
		{
			for(Chunk i : c.getChunks())
			{
				gw.snap(i);
				i.load();
			}

			J.a(() ->
			{
				try
				{
					FileOutputStream fos = new FileOutputStream(f);
					GZIPOutputStream gzo = new GZIPOutputStream(fos);
					DataOutputStream dos = new DataOutputStream(gzo);
					ChronoLatch latch = new ChronoLatch(500, false);
					dos.writeInt(c.getSizeX());
					dos.writeInt(c.getSizeY());
					dos.writeInt(c.getSizeZ());
					dos.writeInt(origin.getBlockX());
					dos.writeInt(origin.getBlockY());
					dos.writeInt(origin.getBlockZ());

					int of = c.volume();
					int did = 0;

					for(int i = min.getBlockX(); i <= max.getBlockX(); i++)
					{
						for(int k = min.getBlockZ(); k <= max.getBlockZ(); k++)
						{
							Chunk ch = at.getWorld().getChunkAt(i >> 4, k >> 4);
							ChunkSnapshot snap = gw.snap(ch);

							for(int j = min.getBlockY(); j <= max.getBlockY(); j++)
							{
								dos.writeByte(snap.getBlockTypeId(i & 15, j, k & 15));
								dos.writeByte(snap.getBlockData(i & 15, j, k & 15));
								did++;
							}

							if(latch.flip())
							{
								percent.run((double) did / (double) of);
							}
						}
					}

					dos.close();
					done.run(true);
				}

				catch(Throwable e)
				{
					done.run(false);
					e.printStackTrace();
				}
			});
		});
	}
}
