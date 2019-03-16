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
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import mortar.api.nms.NMP;
import mortar.api.sched.J;
import mortar.api.world.Cuboid;
import mortar.api.world.Cuboid.CuboidDirection;
import mortar.api.world.MaterialBlock;
import mortar.api.world.VectorMath;
import mortar.compute.math.M;
import mortar.lang.collection.Callback;
import mortar.lang.collection.GList;
import mortar.lang.collection.GMap;
import mortar.logic.queue.ChronoLatch;
import mortar.util.queue.PhantomQueue;

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
		J.s(() ->
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
				PhantomQueue<Chunk> cq = new PhantomQueue<Chunk>().responsiveMode();
				cq.queue(new GList<>(c.getChunks()));
				int vx = cq.size() / 16;
				J.sr(() ->
				{
					for(Chunk i : cq.next(16))
					{
						i.load();
					}
				}, 0, vx);

				J.s(() ->
				{
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

						J.s(() ->
						{
							try
							{
								int signs = din.readInt();

								for(int i = 0; i < signs; i++)
								{
									Location sign = min.clone().add(din.readInt(), din.readInt(), din.readInt());
									Block b = sign.getBlock();

									if(b.getType().equals(Material.SIGN_POST) || b.getType().equals(Material.WALL_SIGN))
									{
										Sign s = (Sign) b.getState();

										for(int j = 0; j < 4; j++)
										{
											s.setLine(j, din.readUTF());
										}

										s.update();
									}

									else
									{
										System.out.println("WARNING MISSING SIGN DATA!");
									}
								}

								J.a(() ->
								{
									try
									{
										din.close();
										gzi.close();
										fin.close();
									}

									catch(IOException e)
									{
										e.printStackTrace();
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
							}

							catch(IOException e1)
							{
								e1.printStackTrace();
							}
						});
					});
				}, vx + 1);
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
		GList<Location> signs = new GList<>();
		GMap<Vector, ChunkSnapshot> chunkCache = new GMap<>();
		PhantomQueue<Chunk> cq = new PhantomQueue<Chunk>().responsiveMode();
		cq.queue(new GList<>(c.outset(CuboidDirection.Horizontal, 1).getChunks()));
		int v = cq.size() / 16;
		J.sr(() ->
		{
			for(Chunk i : cq.next(16))
			{
				chunkCache.put(new Vector(i.getX(), i.getZ(), 0), gw.snap(i));
				i.load();
			}
		}, 0, v);

		J.s(() ->
		{
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
							ChunkSnapshot[] snap = {chunkCache.get(new Vector(i >> 4, k >> 4, 0))};
							int ii = i;
							int kk = k;
							J.s(() ->
							{
								snap[0] = c.getWorld().getChunkAt(ii >> 4, kk >> 4).getChunkSnapshot();
								chunkCache.put(new Vector(ii >> 4, kk >> 4, 0), snap[0]);
							});

							while(snap[0] == null)
							{
								Thread.sleep(1);
							}

							for(int j = min.getBlockY(); j <= max.getBlockY(); j++)
							{
								Material m = snap[0].getBlockType(i & 15, j, k & 15);
								dos.writeByte(m.getId());
								dos.writeByte(snap[0].getBlockData(i & 15, j, k & 15));

								if(m.equals(Material.SIGN_POST) || m.equals(Material.WALL_SIGN))
								{
									signs.add(new Location(c.getWorld(), i, j, k));
								}

								did++;
							}

							if(latch.flip())
							{
								percent.run((double) did / (double) of);
							}
						}
					}

					dos.writeInt(signs.size());

					J.s(() ->
					{
						for(Location i : signs)
						{
							Sign s = (Sign) i.getBlock().getState();

							try
							{
								Vector offset = VectorMath.directionNoNormal(min, i);
								dos.writeInt(offset.getBlockX());
								dos.writeInt(offset.getBlockY());
								dos.writeInt(offset.getBlockZ());

								for(String j : s.getLines())
								{

									dos.writeUTF(j != null ? j : "");
								}
							}

							catch(IOException e)
							{
								e.printStackTrace();

							}
						}

						J.a(() ->
						{
							try
							{
								dos.close();
							}

							catch(IOException e)
							{
								e.printStackTrace();
							}
							done.run(true);
						});
					});
				}

				catch(Throwable e)
				{
					done.run(false);
					e.printStackTrace();
				}
			});
		}, v + 1);
	}
}
