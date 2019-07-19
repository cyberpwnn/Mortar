package mortar.api.scm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import mortar.bukkit.compatibility.MaterialEnum;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import mortar.api.nms.NMP;
import mortar.api.sched.J;
import mortar.api.world.Cuboid;
import mortar.api.world.MaterialBlock;
import mortar.api.world.VectorMath;
import mortar.compute.math.M;
import mortar.lang.collection.Callback;
import mortar.lang.collection.GList;
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

	public static void read(File f, Location at, Callback<Boolean> done, Callback<Double> percent)
	{
		read(f, at, done, percent, true, 500);
	}

	@SuppressWarnings("deprecation")
	public static void read(File f, Location at, Callback<Boolean> done, Callback<Double> percent, boolean loadChunks, long intervalLog)
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
				ChronoLatch latch = new ChronoLatch(intervalLog, false);
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
								try
								{
									int ii = i;
									int kk = k;
									Biome b = Biome.values()[(int) din.readByte()];
									J.s(() -> c.getWorld().setBiome(ii, kk, b));
								}

								catch(IOException e)
								{
									e.printStackTrace();
								}

								for(int j = min.getBlockY(); j <= max.getBlockY(); j++)
								{
									try
									{
										int id = (int) din.readInt();
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

									if(b.getType().equals(MaterialEnum.SIGN_POST.bukkitMaterial()) || b.getType().equals(MaterialEnum.WALL_SIGN.bukkitMaterial()))
									{
										Sign s = (Sign) b.getState();

										for(int j = 0; j < 4; j++)
										{
											s.setLine(j, din.readUTF());
										}

										s.setRawData(din.readByte());
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
									if(loadChunks)
									{
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
									}
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

	@SuppressWarnings({"deprecation", "resource"})
	public static void write(File f, Location at, Cuboid c, Callback<Boolean> done, Callback<Double> percent)
	{
		Location min = c.getLowerNE();
		Location max = c.getUpperSW();
		Vector origin = VectorMath.directionNoNormal(min, at);
		GList<Location> signs = new GList<>();

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
						dos.writeByte(c.getWorld().getBiome(i, k).ordinal());

						for(int j = min.getBlockY(); j <= max.getBlockY(); j++)
						{
							MaterialBlock mb = NMP.host.getBlock(c.getWorld(), i, j, k);
							Material m = mb.getMaterial();
							dos.writeInt(m.getId());
							dos.writeByte(mb.getData());

							if(m.equals(MaterialEnum.SIGN_POST.bukkitMaterial()) || m.equals(MaterialEnum.WALL_SIGN.bukkitMaterial()))
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

							dos.writeByte(s.getRawData());
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
	}
}
