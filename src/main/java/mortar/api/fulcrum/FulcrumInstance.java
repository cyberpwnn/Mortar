package mortar.api.fulcrum;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.common.io.Files;

import mortar.api.atests.BlockExampleCased;
import mortar.api.atests.BlockExampleCauldron;
import mortar.api.atests.BlockExampleCompanion;
import mortar.api.atests.BlockExampleCube;
import mortar.api.atests.BlockExampleFramed;
import mortar.api.atests.BlockExamplePedestal;
import mortar.api.atests.InventoryExampleChest;
import mortar.api.atests.InventoryExampleChestDouble;
import mortar.api.fulcrum.object.CustomBlock;
import mortar.api.fulcrum.object.CustomItem;
import mortar.api.fulcrum.resourcepack.ResourcePack;
import mortar.api.fulcrum.util.BlockSoundCategory;
import mortar.api.fulcrum.util.BlocksScraper;
import mortar.api.fulcrum.util.DigTracker;
import mortar.api.fulcrum.util.IAllocation;
import mortar.api.fulcrum.util.IResource;
import mortar.api.fulcrum.util.PlayerBlockEvent;
import mortar.api.fulcrum.util.PlayerCancelledDiggingEvent;
import mortar.api.fulcrum.util.PlayerFinishedDiggingEvent;
import mortar.api.fulcrum.util.PlayerStartDiggingEvent;
import mortar.api.fulcrum.util.PotentialDropList;
import mortar.api.fulcrum.util.SuperCacheResourceProvider;
import mortar.api.fulcrum.util.ToolLevel;
import mortar.api.fulcrum.util.ToolType;
import mortar.api.nms.Catalyst;
import mortar.api.sched.J;
import mortar.bukkit.plugin.Mortar;
import mortar.bukkit.plugin.MortarAPIPlugin;
import mortar.compute.math.M;
import mortar.lang.json.JSONException;
import mortar.logic.io.Hasher;
import mortar.logic.io.VIO;
import net.minecraft.server.v1_12_R1.PacketPlayInBlockDig;
import net.minecraft.server.v1_12_R1.PacketPlayOutCollect;

public class FulcrumInstance implements Listener
{
	public static String packName;
	public static FulcrumInstance instance;
	private ResourceCache resources;
	private ResourcePack pack;
	private FulcrumRegistry registry;
	private ShittyWebserver web;
	private DigTracker digTracker;
	private BlocksScraper blockScraper;
	private ObfuscationSupplier obfuscationSupplier;
	private SuperCacheResourceProvider resourceProvider;

	public FulcrumInstance() throws JSONException, IOException
	{
		if(instance != null)
		{
			MortarAPIPlugin.p.unregisterListener(this);
		}

		obfuscationSupplier = new ObfuscationSupplier();
		blockScraper = new BlocksScraper();
		digTracker = new DigTracker();
		packName = UUID.randomUUID().toString();
		instance = this;
		resources = new ResourceCache("fcu-" + MortarAPIPlugin.p.getDescription().getVersion());
		resourceProvider = new SuperCacheResourceProvider(getResources().getBase());
		pack = new ResourcePack();
		registry = new FulcrumRegistry();
		VIO.delete(getResources().fileFor("web"));

		Catalyst.host.addIncomingListener("PacketPlayInBlockDig", (sender, packet) ->
		{
			PlayerBlockEvent ex = null;

			if(packet instanceof PacketPlayInBlockDig)
			{
				PacketPlayInBlockDig dig = ((PacketPlayInBlockDig) packet);
				Location l = new Location(sender.getWorld(), dig.a().getX(), dig.a().getY(), dig.a().getZ());
				Block b = l.getBlock();
				BlockFace f = null;

				switch(dig.b())
				{
					case DOWN:
						f = BlockFace.DOWN;
						break;
					case EAST:
						f = BlockFace.EAST;
						break;
					case NORTH:
						f = BlockFace.NORTH;
						break;
					case SOUTH:
						f = BlockFace.SOUTH;
						break;
					case UP:
						f = BlockFace.UP;
						break;
					case WEST:
						f = BlockFace.WEST;
						break;
					default:
						break;
				}

				switch(dig.c())
				{
					case ABORT_DESTROY_BLOCK:
						ex = new PlayerCancelledDiggingEvent(sender, b, f);
						break;
					case START_DESTROY_BLOCK:
						ex = new PlayerStartDiggingEvent(sender, b, f);
						break;
					case STOP_DESTROY_BLOCK:
						ex = new PlayerFinishedDiggingEvent(sender, b, f);
						break;
					default:
						break;
				}

				if(ex != null)
				{
					Bukkit.getPluginManager().callEvent(ex);
				}
			}

			return ex != null ? ex.isCancelled() ? null : packet : packet;
		});
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(PlayerStartDiggingEvent e)
	{
		CustomBlock fu = ContentAssist.getBlock(e.getBlock());

		if(fu != null)
		{
			double speed = ToolLevel.getMiningSpeed(e.getPlayer(), fu, e.getPlayer().getItemInHand());

			if((speed / 20D) * 50D > fu.getHardness())
			{
				J.s(() -> Bukkit.getPluginManager().callEvent(new BlockBreakEvent(e.getBlock(), e.getPlayer())));
			}

			else
			{
				getDigTracker().startedDigging(e.getPlayer(), e.getBlock(), speed);
			}

		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(PlayerCancelledDiggingEvent e)
	{
		CustomBlock fu = ContentAssist.getBlock(e.getBlock());

		if(fu != null)
		{
			getDigTracker().cancelledDigging(e.getPlayer(), e.getBlock());
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(PlayerFinishedDiggingEvent e)
	{
		CustomBlock fu = ContentAssist.getBlock(e.getBlock());

		if(fu != null)
		{
			getDigTracker().finishedDigging(e.getPlayer(), e.getBlock());
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(BlockBreakEvent e)
	{
		CustomBlock f = ContentAssist.getBlock(e.getBlock());

		if(f != null)
		{
			ItemStack is = e.getPlayer().getItemInHand();
			boolean shouldDrop = true;
			int toolLevel = ToolLevel.getToolLevel(is);
			int minLevel = f.getMinimumToolLevel();
			boolean instantBreak = false;
			double speed = ToolLevel.getMiningSpeed(e.getPlayer(), f, is);
			String toolType = ToolType.getType(is);
			String blockTool = f.getEffectiveToolType();
			boolean toolsMatch = toolType.equals(blockTool);

			if((speed / 20D) * 30D > f.getHardness() && toolsMatch)
			{
				instantBreak = true;
			}

			if(toolLevel < minLevel)
			{
				shouldDrop = false;
			}

			if(minLevel > ToolLevel.HAND && !toolsMatch)
			{
				shouldDrop = false;
			}

			if((speed / 20D) * 30D > f.getHardness() && toolsMatch)
			{
				instantBreak = true;
			}

			PotentialDropList pdl = new PotentialDropList();

			if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE) && shouldDrop)
			{
				f.getDrops(e.getBlock(), e.getPlayer(), e.getPlayer().getItemInHand(), pdl);
			}

			f.removeAt(e.getBlock());
			f.playSound(e.getBlock(), BlockSoundCategory.BREAK);
			Location a = e.getBlock().getLocation().clone().add(0.5, 0.5, 0.5);

			if(shouldDrop)
			{
				for(ItemStack i : pdl.computeDrops())
				{
					a.getWorld().dropItemNaturally(a, i);
				}
			}

			if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE))
			{
				getDigTracker().imposeDelay(e.getPlayer(), instantBreak ? 1 : 5);
			}
		}
	}

	public boolean canOverwrite(Material m)
	{
		if(m.equals(Material.LONG_GRASS) || m.equals(Material.AIR) || m.equals(Material.WATER) || m.equals(Material.STATIONARY_WATER) || m.equals(Material.LAVA) || m.equals(Material.STATIONARY_LAVA) || m.equals(Material.DEAD_BUSH))
		{
			return true;
		}

		return false;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(PlayerInteractEvent e)
	{
		if(!isRegistered(e.getItem()))
		{
			return;
		}

		IAllocation a = getRegistered(e.getItem());

		if(a == null)
		{
			return;
		}

		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{
			e.setCancelled(true);

			if(a.isBlock())
			{
				BlockFace f = e.getBlockFace();
				Block c = e.getClickedBlock();
				Block at = c.getWorld().getBlockAt(c.getX() + f.getModX(), c.getY() + f.getModY(), c.getZ() + f.getModZ());

				if(!canOverwrite(at.getType()))
				{
					return;
				}

				if(canOverwrite(c.getType()))
				{
					at = c;
				}

				if(placeAllocation(a.block(), at))
				{
					if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE))
					{
						ItemStack is = e.getItem();
						ItemStack result = is.clone();

						if(is.getAmount() <= 1)
						{
							result = new ItemStack(Material.AIR);
						}

						else
						{
							result.setAmount(is.getAmount() - 1);
						}

						if(e.getHand().equals(EquipmentSlot.HAND))
						{
							e.getPlayer().getInventory().setItemInMainHand(result);
						}

						else
						{
							e.getPlayer().getInventory().setItemInOffHand(result);
						}
					}
				}
			}
		}
	}

	private boolean placeAllocation(CustomBlock block, Block at)
	{
		Block b = at;

		for(Entity i : b.getWorld().getNearbyEntities(b.getLocation().clone().add(0.5, 0.5, 0.5), 0.25, 0.25, 0.25))
		{
			if(i instanceof LivingEntity)
			{
				return false;
			}
		}

		block.placeAt(at);
		block.playSound(at, BlockSoundCategory.PLACE);
		return true;
	}

	public IAllocation getRegistered(ItemStack itemStack)
	{
		return getRegistry().allocator().getAllocation(itemStack.getType(), itemStack.getDurability());
	}

	public IAllocation getRegistered(String id)
	{
		return getRegistry().collective().getRegistry(id);
	}

	public boolean isRegistered(ItemStack itemStack)
	{
		//@builder
		if( itemStack == null ||
				itemStack.getType().getMaxDurability() < 1 ||
				itemStack.getDurability() == 0 ||
				!itemStack.getItemMeta().isUnbreakable() ||
				!getRegistry().allocator().isAllocated(itemStack.getType(), itemStack.getDurability()))
			//@done
		{
			return false;
		}

		return true;
	}

	public void reigsterPack()
	{
		J.a(() ->
		{
			try
			{
				doRegistry();
			}

			catch(Throwable e)
			{
				e.printStackTrace();
			}
		});
	}

	@EventHandler
	public void on(PlayerMoveEvent e)
	{
		if(e.getPlayer().isOnGround() && (e.getTo().getX() != e.getFrom().getX() || e.getTo().getZ() != e.getFrom().getZ()))
		{
			Block b = e.getPlayer().getLocation().add(0, -0.1, 0).getBlock();

			if(b.getType().equals(Fulcrum.SOLID_IDLE))
			{
				CustomBlock cb = ContentAssist.getBlock(b);

				if(cb != null)
				{
					if(M.interval(e.getPlayer().isSneaking() ? 12 : e.getPlayer().isSprinting() ? 5 : 7))
					{
						cb.playSound(b, BlockSoundCategory.STEP);
					}
				}
			}
		}
	}

	public void rebuild() throws NoSuchAlgorithmException, IOException
	{
		File fc = new File("cache/packcache/pack.zip");
		File pack = getResources().fileFor("web/" + packName + ".zip");
		File hashFile = getResources().fileFor("web/" + packName + ".hash");
		fc.getParentFile().mkdirs();
		VIO.writeAll(hashFile, Hasher.bytesToHex(getPack().writeToArchive(pack)));
		Files.copy(pack, fc);
	}

	private void doRegistry() throws Exception
	{
		getPack().getMeta().setRevision(UUID.randomUUID().toString().split("-")[1]);
		getPack().getMeta().setProduction(false);
		getPack().getMeta().setVendorName("Your Server");
		getPack().getMeta().setVendorURL("yourserver.net");
		getPack().getMeta().setPackDescription("Some pack description");
		getPack().getMeta().setPackFormat(3);
		getRegistry().begin();
		registerExamples();
		registerBreakBlocks();
		getRegistry().complete();
		MortarAPIPlugin.p.registerListener(this);
		web = new ShittyWebserver(Fulcrum.webServerPort, getResources().fileFor("web"));
		web.start();
		getPack().setOptimizePngs(Fulcrum.optimizeImages);
		getPack().setOverbose(Fulcrum.verbose);
		getPack().setMinifyJSON(Fulcrum.minifyJSON);
		getPack().setObfuscate(Fulcrum.obfuscate);
		getPack().setDeduplicate(Fulcrum.deduplicate);
		File fc = new File("cache/packcache/pack.zip");
		File pack = getResources().fileFor("web/" + packName + ".zip");
		File hashFile = getResources().fileFor("web/" + packName + ".hash");
		fc.getParentFile().mkdirs();
		getPack().o("Pack Cached: " + fc.exists() + " " + fc.getAbsolutePath());
		getPack().o("Just Started: " + Mortar.STARTUP_LOAD);

		if(fc.exists() && !Mortar.STARTUP_LOAD)
		{
			Files.copy(fc, pack);
			getPack().o("Using Cached Resource pack. Use /fu rebuild to force rebuild!");
		}

		else
		{
			VIO.writeAll(hashFile, Hasher.bytesToHex(getPack().writeToArchive(pack)));
			Files.copy(pack, fc);
		}
	}

	private void registerBreakBlocks()
	{
		for(int i = 0; i < 10; i++)
		{
			getRegistry().block().register(new BlockOverlayBreak(i));
		}
	}

	private void registerExamples()
	{
		if(Fulcrum.registerExamples)
		{
			getRegistry().block().register(new BlockExampleCube());
			getRegistry().block().register(new BlockExampleFramed());
			getRegistry().block().register(new BlockExampleCased());
			getRegistry().block().register(new BlockExampleCompanion());
			getRegistry().block().register(new BlockExampleCauldron());
			getRegistry().block().register(new BlockExamplePedestal());
			getRegistry().inventory().register(new InventoryExampleChest());
			getRegistry().inventory().register(new InventoryExampleChestDouble());
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(InventoryDragEvent e)
	{
		ItemStack is = e.getOldCursor();

		if(is != null && isRegistered(is))
		{
			CustomItem item = (CustomItem) getRegistered(is);
			int count = item.getMaxStackSize();
			int left = e.getOldCursor().getAmount();
			int div = e.getRawSlots().size();
			int f = left / div;

			if(f <= 1)
			{
				return;
			}

			e.setCancelled(true);
			for(int i : e.getRawSlots())
			{
				int place = Math.min(f, count);
				ItemStack ix = e.getView().getItem(i);

				if(ix == null || ix.getType().equals(Material.AIR))
				{
					ItemStack iv = is.clone();
					iv.setAmount(place);
					e.getView().setItem(i, iv);

					left -= place;
				}
			}

			int ll = left;

			J.s(() ->
			{
				if(ll == 0)
				{
					e.getWhoClicked().setItemOnCursor(null);
				}

				else
				{
					ItemStack ss = e.getWhoClicked().getItemOnCursor().clone();
					ss.setAmount(ll);
					e.getWhoClicked().setItemOnCursor(ss);
				}
			});
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(EntityPickupItemEvent e)
	{
		if(e.getEntity() instanceof Player)
		{
			Player p = (Player) e.getEntity();

			if(isRegistered(e.getItem().getItemStack()))
			{
				ItemStack is = e.getItem().getItemStack().clone();
				e.setCancelled(true);
				addToInventory(p.getInventory(), is);
				Catalyst.host.sendRangedPacket(16, e.getEntity().getLocation(), new PacketPlayOutCollect(e.getItem().getEntityId(), e.getEntity().getEntityId(), 1));
				e.getItem().remove();
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(InventoryClickEvent e)
	{
		if(e.getCurrentItem() == null)
		{
			return;
		}

		ItemStack is = e.getCurrentItem().clone();
		ItemStack cursor = e.getCursor();
		Inventory top = e.getView().getTopInventory();
		Inventory bottom = e.getView().getBottomInventory();
		Inventory clickedInventory = e.getClickedInventory();
		int clickedSlot = e.getSlot();

		if(e.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY))
		{
			if(bottom != null && top != null)
			{
				Inventory other = bottom.equals(clickedInventory) ? top : bottom;

				if(is != null && isRegistered(is))
				{
					J.s(() -> stack(is, other, e.getWhoClicked(), 0));
				}
			}
		}

		if(e.getAction().equals(InventoryAction.COLLECT_TO_CURSOR))
		{
			if(cursor != null && isRegistered(cursor))
			{
				int stack = ((CustomItem) getRegistered(cursor)).getMaxStackSize();

				ItemStack[] isx = e.getClickedInventory().getContents();

				for(int i = 0; i < isx.length; i++)
				{
					ItemStack isv = isx[i];

					if(isv != null && isv.getType().equals(cursor.getType()) && isv.getDurability() == cursor.getDurability() && cursor.getItemMeta().isUnbreakable() == true && cursor.getItemMeta().isUnbreakable() == isv.getItemMeta().isUnbreakable())
					{
						if(cursor.getAmount() < stack)
						{
							if(cursor.getAmount() + isv.getAmount() <= stack)
							{
								cursor.setAmount(cursor.getAmount() + isv.getAmount());
								e.getClickedInventory().setItem(i, new ItemStack(Material.AIR));
								e.setCursor(cursor.clone());
							}
						}

						else
						{
							break;
						}
					}
				}

				if(cursor.getAmount() < stack && bottom != null && top != null)
				{
					Inventory other = bottom.equals(clickedInventory) ? top : bottom;

					isx = other.getContents();

					for(int i = 0; i < isx.length; i++)
					{
						ItemStack isv = isx[i];

						if(isv != null && isv.getType().equals(cursor.getType()) && isv.getDurability() == cursor.getDurability() && cursor.getItemMeta().isUnbreakable() == true && cursor.getItemMeta().isUnbreakable() == isv.getItemMeta().isUnbreakable())
						{
							if(cursor.getAmount() < stack)
							{
								if(cursor.getAmount() + isv.getAmount() <= stack)
								{
									cursor.setAmount(cursor.getAmount() + isv.getAmount());
									other.setItem(i, new ItemStack(Material.AIR));
									e.setCursor(cursor.clone());
								}
							}

							else
							{
								break;
							}
						}
					}
				}
			}
		}

		if(e.getAction().equals(InventoryAction.NOTHING) || e.getAction().equals(InventoryAction.PICKUP_SOME) || e.getAction().equals(InventoryAction.PICKUP_ONE))
		{
			if(e.getClick().equals(ClickType.RIGHT))
			{
				if(cursor != null && isRegistered(cursor))
				{
					if(is != null && cursor.getType().equals(is.getType()) && cursor.getDurability() == is.getDurability() && cursor.getItemMeta().isUnbreakable() && is.getItemMeta().isUnbreakable())
					{
						e.setCancelled(true);

						if(cursor != null && isRegistered(cursor))
						{
							if(is != null && cursor.getType().equals(is.getType()) && cursor.getDurability() == is.getDurability() && cursor.getItemMeta().isUnbreakable() && is.getItemMeta().isUnbreakable())
							{
								int count = ((CustomItem) getRegistered(cursor)).getMaxStackSize();
								int maxPull = count - is.getAmount();

								if(cursor.getAmount() == 1 && is.getAmount() < count)
								{
									is.setAmount(is.getAmount() + 1);
									clickedInventory.setItem(clickedSlot, is.clone());
									e.setCursor(new ItemStack(Material.AIR));
								}

								else if(maxPull > 0 && cursor.getAmount() > 1)
								{
									cursor.setAmount(cursor.getAmount() - 1);
									e.setCursor(cursor.clone());
									is.setAmount(is.getAmount() + 1);
									clickedInventory.setItem(clickedSlot, is.clone());
								}
							}
						}

					}
				}
			}

			if(e.getClick().equals(ClickType.LEFT))
			{
				if(cursor != null && isRegistered(cursor))
				{
					if(is != null && cursor.getType().equals(is.getType()) && cursor.getDurability() == is.getDurability() && cursor.getItemMeta().isUnbreakable() && is.getItemMeta().isUnbreakable())
					{
						e.setCancelled(true);

						if(cursor != null && isRegistered(cursor))
						{
							if(is != null && cursor.getType().equals(is.getType()) && cursor.getDurability() == is.getDurability() && cursor.getItemMeta().isUnbreakable() && is.getItemMeta().isUnbreakable())
							{
								int count = ((CustomItem) getRegistered(cursor)).getMaxStackSize();
								int maxPull = count - is.getAmount();

								while(maxPull > 0 && cursor.getAmount() > 1)
								{
									cursor.setAmount(cursor.getAmount() - 1);
									e.setCursor(cursor.clone());
									is.setAmount(is.getAmount() + 1);
									clickedInventory.setItem(clickedSlot, is.clone());
								}

								if(cursor.getAmount() == 1 && is.getAmount() < count)
								{
									is.setAmount(is.getAmount() + 1);
									clickedInventory.setItem(clickedSlot, is.clone());
									e.setCursor(new ItemStack(Material.AIR));
								}
							}
						}

					}
				}
			}
		}
	}

	public void stop()
	{
		try
		{
			web.stop();
		}

		catch(Throwable e)
		{

		}
	}

	public ShittyWebserver getWeb()
	{
		return web;
	}

	public FulcrumRegistry getRegistry()
	{
		return registry;
	}

	public ResourceCache getResources()
	{
		return resources;
	}

	public ResourcePack getPack()
	{
		return pack;
	}

	public DigTracker getDigTracker()
	{
		return digTracker;
	}

	public BlocksScraper getBlockScraper()
	{
		return blockScraper;
	}

	public void addToInventory(Inventory inv, ItemStack is, int hint)
	{
		ItemStack[] iss = inv.getContents();
		CustomItem it = (CustomItem) getRegistered(is);
		int left = is.getAmount();
		int z = -1;

		if(it != null)
		{
			z = it.getMaxStackSize();
		}

		if(it == null)
		{
			inv.addItem(is);
			return;
		}

		for(int i = 0; i < iss.length; i++)
		{
			if(left == 0)
			{
				break;
			}

			if(iss[i] != null)
			{
				ItemStack ic = iss[i].clone();

				if(ic.getType().equals(is.getType()) && ic.getDurability() == is.getDurability() && ic.getItemMeta().isUnbreakable())
				{
					while(ic.getAmount() < z && left > 0)
					{
						ic.setAmount(ic.getAmount() + 1);
						left--;
					}

					iss[i] = ic.clone();
				}
			}
		}

		inv.setContents(iss);

		while(left > 0)
		{
			ItemStack ix = is.clone();
			ix.setAmount(Math.min(left, z));
			left = left - Math.min(left, z);

			if(inv.getContents()[hint] == null)
			{
				inv.setItem(hint, ix);
			}

			else
			{
				inv.addItem(ix);
			}
		}
	}

	public void addToInventory(Inventory inv, ItemStack is)
	{
		addToInventory(inv, is, 0);
	}

	public void stack(ItemStack ist, Inventory inv, HumanEntity e, int hintSlot)
	{
		int count = 0;
		ItemStack[] isx = inv.getContents();
		ItemStack demo = ist.clone();

		for(int i = 0; i < isx.length; i++)
		{
			ItemStack is = isx[i];

			if(is != null && is.getType().equals(ist.getType()) && is.getDurability() == ist.getDurability() && ist.getItemMeta().isUnbreakable() && is.getItemMeta().isUnbreakable())
			{
				count += is.getAmount();
				inv.setItem(i, new ItemStack(Material.AIR));
			}
		}

		while(count > 0)
		{
			int a = Math.min(count, 64);
			demo.setAmount(a);
			count -= a;
			addToInventory(inv, demo.clone(), hintSlot);
		}

		if(e instanceof Player)
		{
			((Player) e).updateInventory();
		}
	}

	public IResource getResource(String path)
	{
		return getResourceProvider().get(path);
	}

	public ObfuscationSupplier getObfuscationSupplier()
	{
		return obfuscationSupplier;
	}

	public SuperCacheResourceProvider getResourceProvider()
	{
		return resourceProvider;
	}
}
