package mortar.bukkit.plugin;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import mortar.api.world.BlockType;
import mortar.scm.GhostWorld;

public class GhostWorldController extends Controller
{
	private GhostWorld world;

	@Override
	public void start()
	{
		world = new GhostWorld();
		setTickRate(0);
	}

	@Override
	public void stop()
	{
		world.drop(-1);
	}

	@Override
	public void tick()
	{
		world.drop(10000);
	}

	@EventHandler
	public void on(BlockPlaceEvent e)
	{
		world.drop(e.getBlock().getChunk());
	}

	@EventHandler
	public void on(BlockBreakEvent e)
	{
		world.drop(e.getBlock().getChunk());
	}

	@EventHandler
	public void on(BlockBurnEvent e)
	{
		world.drop(e.getBlock().getChunk());
	}

	@EventHandler
	public void on(BlockExplodeEvent e)
	{
		world.drop(e.getBlock().getChunk());
	}

	@EventHandler
	public void on(BlockFadeEvent e)
	{
		world.drop(e.getBlock().getChunk());
	}

	public BlockType get(Location l)
	{
		return world.get(l);
	}
}
