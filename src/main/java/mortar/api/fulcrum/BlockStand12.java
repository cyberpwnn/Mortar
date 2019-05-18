package mortar.api.fulcrum;

import net.minecraft.server.v1_12_R1.DamageSource;
import net.minecraft.server.v1_12_R1.EntityArmorStand;
import net.minecraft.server.v1_12_R1.World;

public class BlockStand12 extends EntityArmorStand
{
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
	public void killEntity()
	{
		// No
	}
}
