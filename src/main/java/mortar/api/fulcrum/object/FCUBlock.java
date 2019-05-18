package mortar.api.fulcrum.object;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import mortar.api.fulcrum.BlockStand12;
import mortar.api.fulcrum.util.BlockCollision;
import mortar.api.resourcepack.ModelType;
import mortar.api.resourcepack.TextureType;
import mortar.lang.collection.GMap;

public class FCUBlock extends FCUItem
{
	private BlockCollision collisionMode;

	public FCUBlock(String id)
	{
		super(id);
		textures = new GMap<>();
		setTexture(id, "assets/textures/blocks/unknown.png");
		setModel(id, "assets/models/block/default_cube_all.json");
		model.rewrite("$id", id);
		setCollisionMode(BlockCollision.FULL);
	}

	public BlockCollision getCollisionMode()
	{
		return collisionMode;
	}

	public void setCollisionMode(BlockCollision collisionMode)
	{
		this.collisionMode = collisionMode;
	}

	@Override
	public void setTexture(String name, String resource)
	{
		textures.put(name, new FCUTexture(name, getClass(), resource, TextureType.BLOCKS));
	}

	@Override
	public void setModel(String name, String resource)
	{
		model = new FCUModel(name, getClass(), resource, ModelType.BLOCK);
	}

	@Override
	public String getAllocatedModel()
	{
		return "block/" + model.getModelName();
	}

	@Override
	public FCUBlock block()
	{
		return (FCUBlock) this;
	}

	@Override
	public boolean isBlock()
	{
		return true;
	}

	public void placeAt(Block at)
	{
		Location l = at.getLocation().clone().add(0.5, 0, 0.5);
		BlockStand12 entity = new BlockStand12(((CraftWorld) l.getWorld()).getHandle(), l.getX(), l.getY(), l.getZ());
		entity.yaw = 0;
		entity.lastYaw = 0;
		entity.pitch = 0;
		entity.lastPitch = 0;
		entity.positionChanged = false;

		if(entity.world.addEntity(entity, SpawnReason.CUSTOM))
		{
			ArmorStand a = (ArmorStand) entity.getBukkitEntity();
			a.setAI(false);
			a.setGravity(false);
			a.setBasePlate(false);
			a.setArms(false);
			a.setSmall(true);
			a.setMarker(true);
			a.setInvulnerable(true);
			a.setVisible(false);
			a.setCanPickupItems(false);
			a.setCollidable(false);
			a.setRemoveWhenFarAway(false);
			a.setSilent(true);
			a.setHelmet(toItemStack(1));

			if(getCollisionMode().equals(BlockCollision.FULL))
			{
				l.getBlock().setType(Material.BARRIER);

			}
		}
	}
}
