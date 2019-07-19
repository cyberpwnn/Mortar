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
	STONE("STONE"),
	BARRIER("BARRIER"),
	SULPHUR("SULPHUR", "GUNPOWDER"),
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
	BOOK_AND_QUILL("BOOK_AND_QUILL", "WRITABLE_BOOK", "LEGACY_BOOK_AND_QUILL"),
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