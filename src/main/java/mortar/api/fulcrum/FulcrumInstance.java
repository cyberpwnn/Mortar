package mortar.api.fulcrum;

import java.io.File;
import java.util.UUID;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import mortar.api.fulcrum.object.FCUBlock;
import mortar.api.fulcrum.util.EntityNMS12;
import mortar.api.fulcrum.util.EntityNMS12.Type;
import mortar.api.fulcrum.util.IAllocation;
import mortar.api.resourcepack.ResourcePack;
import mortar.api.sched.J;
import mortar.bukkit.plugin.MortarAPIPlugin;
import mortar.logic.io.Hasher;
import mortar.logic.io.VIO;

public class FulcrumInstance implements Listener
{
	public static String packName;
	public static FulcrumInstance instance;
	private ResourceCache resources;
	private ResourcePack pack;
	private FulcrumRegistry registry;
	private ShittyWebserver web;

	public FulcrumInstance()
	{
		if(instance != null)
		{
			MortarAPIPlugin.p.unregisterListener(this);
		}

		packName = UUID.randomUUID().toString();
		instance = this;
		resources = new ResourceCache("fcu-" + MortarAPIPlugin.p.getDescription().getVersion());
		pack = new ResourcePack();
		registry = new FulcrumRegistry();
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
				placeAllocation(a.block(), e.getClickedBlock().getWorld().getBlockAt(e.getClickedBlock().getX() + e.getBlockFace().getModX(), e.getClickedBlock().getY() + e.getBlockFace().getModY(), e.getClickedBlock().getZ() + e.getBlockFace().getModZ()));
			}

			else
			{
				e.getPlayer().sendMessage("no block");
			}
		}
	}

	private void placeAllocation(FCUBlock block, Block at)
	{
		block.placeAt(at);
	}

	public IAllocation getRegistered(ItemStack itemStack)
	{
		return getRegistry().allocator().getAllocation(itemStack.getType(), itemStack.getDurability());
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

	private void doRegistry() throws Exception
	{
		EntityNMS12.registerEntity("block_stand", Type.ARMOR_STAND, BlockStand12.class);
		getRegistry().begin().complete();
		getPack().getMeta().setPackFormat(3);
		getPack().getMeta().setPackDescription("Some pack description");
		getPack().setOptimizePngs(Fulcrum.optimizeImages);
		getPack().setOverbose(Fulcrum.verbose);
		File pack = getResources().fileFor("web/" + packName + ".zip");
		File hashFile = getResources().fileFor("web/" + packName + ".hash");
		VIO.writeAll(hashFile, Hasher.bytesToHex(getPack().writeToArchive(pack)));
		MortarAPIPlugin.p.registerListener(this);
		web = new ShittyWebserver(Fulcrum.webServerPort, getResources().fileFor("web"));
		web.start();
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
}
