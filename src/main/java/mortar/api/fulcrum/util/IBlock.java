package mortar.api.fulcrum.util;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IBlock
{
	public void removeAt(Block at);

	public ArmorStand placeAt(Block at);

	public void getDrops(Block at, Player who, ItemStack tool, PotentialDropList drops);

	public String getEffectiveToolType();

	public void setEffectiveToolType(String toolType);

	public double getHardness();

	public void setHardness(double hardness);

	public int getMinimumToolLevel();

	public void setMinimumToolLevel(int minimumToolLevel);

	public void setHardnessLike(Material m);

	public void setEffectiveToolLike(Material m);

	public BlockCollision getCollisionMode();

	public void setCollisionMode(BlockCollision collisionMode);
}
