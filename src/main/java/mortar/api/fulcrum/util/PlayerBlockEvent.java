package mortar.api.fulcrum.util;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import mortar.event.MortarCancellableEvent;

public class PlayerBlockEvent extends MortarCancellableEvent
{
	private final Player player;
	private final Block block;
	private final BlockFace face;

	public PlayerBlockEvent(Player player, Block block, BlockFace face)
	{
		this.player = player;
		this.block = block;
		this.face = face;
	}

	public Player getPlayer()
	{
		return player;
	}

	public Block getBlock()
	{
		return block;
	}

	public BlockFace getFace()
	{
		return face;
	}
}
