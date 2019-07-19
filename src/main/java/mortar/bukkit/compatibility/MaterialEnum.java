package mortar.bukkit.compatibility;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

/**
 * Provides material compatibility across multiple CraftBukkit
 * versions due to inconsistencies and enums being renamed.
 *
 * @author David Tkachuk <davidtkachuk@gmail.com>
 * @since Mortar 1.0.70
 */
public enum MaterialEnum
{

	AIR("AIR"),
	TNT("TNT"),
	DIRT("DIRT"),
	STONE("STONE"),
	BARRIER("BARRIER"),
	SULPHUR("SULPHUR", "GUNPOWDER"),
	WATER("WATER"),
	PAPER("PAPER"),
	STATIONARY_WATER("STATIONARY_WATER", "LEGACY_STATIONARY_WATER"),
	HOPPER("HOPPER"),
	STONE_SWORD("STONE_SWORD"),
	IRON_PICKAXE("IRON_PICKAXE"),
	SKULL_ITEM("SKULL_ITEM", "PLAYER_HEAD"),
	STAINED_GLASS_PANE("STAINED_GLASS_PANE", "LEGACY_STAINED_GLASS_PANE"),
	EXP_BOTTLE("EXP_BOTTLE", "EXPERIENCE_BOTTLE"),
	DIAMOND_SWORD("DIAMOND_SWORD"),
	INK_SAC("INK_SAC", "INK_SACK"),
	PAINTING("PAINTING"),
	ARROW("ARROW"),
	SNOWBALL("SNOW_BALL", "SNOWBALL"),
	FIREBALL("FIREBALL", "FIRE_CHARGE"),
	ENDER_PEARL("ENDER_PEARL"),
	FIREWORKK("FIREWORK", "FIREWORK_ROCKET"),
	BOAT("BOAT", "OAK_BOAT"),
	EGG("EGG"),
	IRON_BLOCK("IRON_BLOCK"),
	BOOK_AND_QUILL("BOOK_AND_QUILL", "WRITABLE_BOOK", "LEGACY_BOOK_AND_QUILL"),
	WRITTEN_BOOK("WRITTEN_BOOK"),
	IRON_AXE("IRON_AXE"),
	BEDROCK("BEDROCK"),
	PURPUR_BLOCK("PURPUR_BLOCK"),
	END_BRICKS("END_BRICKS", "END_STONE_BRICKS"),
	LAVA("LAVA"),
	STATIONARY_LAVA("STATIONARY_LAVA", "LEGACY_STATIONARY_LAVA"),
	SIGN_POST("SIGN_POST", "LEGACY_SIGN_POST"),
	WALL_SIGN("WALL_SIGN", "LEGACY_WALL_SIGN"),
	DIAMOND_HOE("DIAMOND_HOE"),
	IRON_HOE("IRON_HOE"),
	STONE_HOE("STONE_HOE"),
	GOLD_HOE("GOLD_HOE", "GOLDEN_HOE"),
	WOOD_HOE("WOOD_HOE", "WOODEN_HOE"),
	DEAD_BUSH("DEAD_BUSH", "LEGACY_DEAD_BUSH"),
	LONG_GRASS("LONG_GRASS", "TALL_GRASS", "LEGACY_LONG_GRASS"),
	MOB_SPAWNER("MOB_SPAWNER", "SPAWNER");

	private String[] versionDependentNames;
	private Material cached = null;

	MaterialEnum(String... versionDependentNames)
	{
		this.versionDependentNames = versionDependentNames;
	}

	/**
	 * Get the Bukkit Material type for current server version
	 *
	 * Caches the bukkit material type after initial run
	 * @return corresponding {@link EntityType}
	 */
	public Material bukkitMaterial()
	{
		if (cached != null) return cached;
		for (String name : versionDependentNames) {
			try {
				return cached = Material.valueOf(name);
			} catch (IllegalArgumentException ignore2) {
				// try next
			}
		}
		throw new IllegalArgumentException("Found no valid Material Type for " + this.name());
	}

}