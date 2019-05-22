package mortar.api.fulcrum;

import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;

import mortar.api.fulcrum.object.CustomBlock;
import mortar.lang.collection.GSet;
import net.minecraft.server.v1_12_R1.DamageSource;
import net.minecraft.server.v1_12_R1.EntityArmorStand;
import net.minecraft.server.v1_12_R1.World;

public class BlockStand12 extends EntityArmorStand
{
	public static GSet<Integer> allowDelete = new GSet<>();

	public BlockStand12(World world, double d0, double d1, double d2)
	{
		super(world, d0, d1, d2);
	}

	public BlockStand12(World world)
	{
		super(world);
	}

	@Override
	public boolean damageEntity(DamageSource damagesource, float f)
	{
		return false;
	}

	@Override
	public void die()
	{
		super.die();
		ArmorStand a = ((ArmorStand) getBukkitEntity());

		if(allowDelete.contains(a.getEntityId()))
		{
			allowDelete.remove(a.getEntityId());
			return;
		}

		Block b = a.getLocation().getBlock();
		CustomBlock cb = (CustomBlock) FulcrumInstance.instance.getRegistered(a.getHelmet());

		if(cb != null)
		{
			mortar.api.sched.J.s(() -> cb.replaceAt(b));
		}
	}
}
