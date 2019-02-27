package mortar.api.rift;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.spigotmc.SpigotWorldConfig;
import org.spigotmc.TickLimiter;

import mortar.api.nms.NMSX;

public class PhysicsEngine
{
	private Rift rift;
	private PhysicsThrottle throttle;
	private SpecializedTickLimiter tEntity;
	private SpecializedTickLimiter tTile;

	public PhysicsEngine(Rift rift)
	{
		this.rift = rift;
	}

	public void inject() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException
	{
		throttle = new PhysicsThrottle(rift);
		tEntity = new SpecializedTickLimiter(rift.getEntityTickLimit());
		tTile = new SpecializedTickLimiter(rift.getTileTickLimit());
		throttle.setDelay(rift.getPhysicsThrottle());
		Class<?> cworldclass = NMSX.getCBClass("CraftWorld");
		Object theWorld = cworldclass.getMethod("getHandle").invoke(rift.getWorld());
		SpigotWorldConfig wc = (SpigotWorldConfig) theWorld.getClass().getField("spigotConfig").get(theWorld);
		wc.tileMaxTickTime = 1;
		wc.entityMaxTickTime = 1;
		wc.maxTntTicksPerTick = rift.getMaxTNTUpdatesPerTick();
		wc.saveStructureInfo = false;
		wc.animalActivationRange = rift.getAnimalActivationRange();
		wc.miscActivationRange = rift.getMiscActivationRange();
		wc.monsterActivationRange = rift.getMonsterActivationRange();
		wc.tickInactiveVillagers = false;
		wc.arrowDespawnRate = rift.getArrowDespawnRate();
		wc.expMerge = rift.getXPMergeRadius();
		wc.itemMerge = rift.getItemMergeRadius();
		wc.itemDespawnRate = rift.getItemDespawnRate();
		wc.viewDistance = rift.getViewDistance();
		wc.hopperTransfer = rift.getHopperTransferRate();
		wc.hopperAmount = rift.getHopperTransferAmount();
		wc.hopperCheck = rift.getHopperCheckRate();
		wc.hangingTickFrequency = rift.getHangingTickRate();
		wc.nerfSpawnerMobs = rift.isNerfSpawnerMobs();
		wc.playerTrackingRange = rift.getPlayerTrackingRange();
		wc.randomLightUpdates = rift.isRandomLightUpdates();
		Field fe = deepFindField(theWorld, "entityLimiter");
		Field ft = deepFindField(theWorld, "tileLimiter");
		fe.setAccessible(true);
		ft.setAccessible(true);
		fe.set(theWorld, tEntity);
		ft.set(theWorld, tTile);
	}

	public void reset() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		throttle.dumpTicklist();
		Class<?> cworldclass = NMSX.getCBClass("CraftWorld");
		Object theWorld = cworldclass.getMethod("getHandle").invoke(rift.getWorld());
		Field fe = deepFindField(theWorld, "entityLimiter");
		Field ft = deepFindField(theWorld, "tileLimiter");
		TickLimiter ste = new TickLimiter(5);
		TickLimiter stt = new TickLimiter(5);
		fe.setAccessible(true);
		ft.setAccessible(true);
		fe.set(theWorld, ste);
		ft.set(theWorld, stt);
	}

	public void update()
	{
		if(rift.isLoaded())
		{
			throttle.tick();
			throttle.setDelay(rift.getPhysicsThrottle());
			tEntity.setrMaxTime(rift.getEntityTickLimit());
			tTile.setrMaxTime(rift.getTileTickLimit());
		}
	}

	private Field deepFindField(Object obj, String fieldName)
	{
		Class<?> cls = obj.getClass();

		for(Class<?> acls = cls; acls != null; acls = acls.getSuperclass())
		{
			try
			{
				Field field = acls.getDeclaredField(fieldName);

				return field;
			}

			catch(NoSuchFieldException ex)
			{

			}
		}

		return null;
	}

	public double getEntityTime()
	{
		return tEntity.getAtimes().getAverage();
	}

	public double getTileTime()
	{
		return tTile.getAtimes().getAverage();
	}
}
