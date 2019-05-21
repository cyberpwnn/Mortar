package mortar.api.fulcrum.util;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class PlayerCancelledDiggingEvent extends PlayerBlockEvent
{
	public PlayerCancelledDiggingEvent(Player player, Block block, BlockFace f)
	{
		super(player, block, f);
	}
}
