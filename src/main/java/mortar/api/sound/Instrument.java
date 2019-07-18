package mortar.api.sound;

import mortar.bukkit.compatibility.SoundEnum;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public enum Instrument
{
	ENCHANT_HIGH(new MFADistortion(4, 1.9f).distort(new Audio().s(SoundEnum.BLOCK_ENCHANTMENT_TABLE_USE.bukkitSound()).vp(1f, 1.5f))),
	ENCHANT_MID(new MFADistortion(4, 1.1f).distort(new Audio().s(SoundEnum.BLOCK_ENCHANTMENT_TABLE_USE.bukkitSound()).vp(1f, 1.5f))),
	ENCHANT_LOW(new MFADistortion(4, 0.11f).distort(new Audio().s(SoundEnum.BLOCK_ENCHANTMENT_TABLE_USE.bukkitSound()).vp(1f, 1.5f))),
	CLICK(new MFADistortion(6, 0.1f).distort(new Audio().s(SoundEnum.BLOCK_LEVER_CLICK.bukkitSound()).vp(1f, 1.7f))),
	KINDLE_HIGH(new MFADistortion(6, 1.9f).distort(new Audio().s(SoundEnum.BLOCK_FURNACE_FIRE_CRACKLE.bukkitSound()).vp(1f, 1f))),
	KINDLE_MID(new MFADistortion(6, 1.5f).distort(new Audio().s(SoundEnum.BLOCK_FURNACE_FIRE_CRACKLE.bukkitSound()).vp(1f, 0.6f))),
	KINDLE_LOW(new MFADistortion(6, 0.7f).distort(new Audio().s(SoundEnum.BLOCK_FURNACE_FIRE_CRACKLE.bukkitSound()).vp(1f, 0.1f))),
	KINDLE_WIDE(new MFADistortion(6, 0.1f).distort(new Audio().s(SoundEnum.BLOCK_FURNACE_FIRE_CRACKLE.bukkitSound()).vp(1f, 1.9f))),
	GRAVEL_HIGH(new MFADistortion(6, 1.9f).distort(new Audio().s(SoundEnum.STEP_SAND.bukkitSound()).vp(1f, 1f))),
	GRAVEL_MID(new MFADistortion(6, 1.5f).distort(new Audio().s(SoundEnum.STEP_SAND.bukkitSound()).vp(1f, 0.6f))),
	GRAVEL_LOW(new MFADistortion(6, 0.7f).distort(new Audio().s(SoundEnum.STEP_SAND.bukkitSound()).vp(1f, 0.1f))),
	GRAVEL_WIDE(new MFADistortion(6, 0.1f).distort(new Audio().s(SoundEnum.STEP_SAND.bukkitSound()).vp(1f, 1.9f))),
	BWANG_HIGH(new MFADistortion(6, 1.9f).distort(new Audio().s(SoundEnum.ARROW_HIT.bukkitSound()).vp(1f, 1f))),
	BWANG_MID(new MFADistortion(6, 1.5f).distort(new Audio().s(SoundEnum.ARROW_HIT.bukkitSound()).vp(1f, 0.6f))),
	BWANG_LOW(new MFADistortion(6, 0.7f).distort(new Audio().s(SoundEnum.ARROW_HIT.bukkitSound()).vp(1f, 0.1f))),
	POCKET_HIGH(new MFADistortion(6, 1.9f).distort(new Audio().s(SoundEnum.ENTITY_DONKEY_CHEST.bukkitSound()).vp(1f, 1f))),
	POCKET_MID(new MFADistortion(6, 1.5f).distort(new Audio().s(SoundEnum.ENTITY_DONKEY_CHEST.bukkitSound()).vp(1f, 0.6f))),
	POCKET_LOW(new MFADistortion(6, 0.7f).distort(new Audio().s(SoundEnum.ENTITY_DONKEY_CHEST.bukkitSound()).vp(1f, 0.1f))),
	SWEEP_HIGH(new MFADistortion(6, 1.9f).distort(new Audio().s(SoundEnum.ENTITY_PLAYER_ATTACK_SWEEP.bukkitSound()).vp(1f, 1f))),
	SWEEP_MID(new MFADistortion(6, 1.5f).distort(new Audio().s(SoundEnum.ENTITY_PLAYER_ATTACK_SWEEP.bukkitSound()).vp(1f, 0.6f))),
	SWEEP_LOW(new MFADistortion(6, 0.7f).distort(new Audio().s(SoundEnum.ENTITY_PLAYER_ATTACK_SWEEP.bukkitSound()).vp(1f, 0.1f))),
	BOB_CLOSE_HIGH(new MFADistortion(6, 1.9f).distort(new Audio().s(SoundEnum.ENTITY_BOBBER_THROW.bukkitSound()).vp(1f, 1f))),
	BOB_CLOSE_MID(new MFADistortion(6, 1.5f).distort(new Audio().s(SoundEnum.ENTITY_BOBBER_THROW.bukkitSound()).vp(1f, 0.6f))),
	BOB_CLOSE_LOW(new MFADistortion(6, 0.7f).distort(new Audio().s(SoundEnum.ENTITY_BOBBER_THROW.bukkitSound()).vp(1f, 0.1f))),
	THICK_CLOSE_HIGH(new MFADistortion(6, 1.9f).distort(new Audio().s(SoundEnum.ENTITY_SHULKER_CLOSE.bukkitSound()).vp(1f, 1f))),
	THICK_CLOSE_MID(new MFADistortion(6, 1.5f).distort(new Audio().s(SoundEnum.ENTITY_SHULKER_CLOSE.bukkitSound()).vp(1f, 0.6f))),
	THICK_CLOSE_LOW(new MFADistortion(6, 0.7f).distort(new Audio().s(SoundEnum.ENTITY_SHULKER_CLOSE.bukkitSound()).vp(1f, 0.1f))),
	BRITTLE_HIGH(new MFADistortion(6, 1.9f).distort(new Audio().s(SoundEnum.ENTITY_ARMORSTAND_BREAK.bukkitSound()).vp(1f, 1f))),
	BRITTLE_MID(new MFADistortion(6, 1.5f).distort(new Audio().s(SoundEnum.ENTITY_ARMORSTAND_BREAK.bukkitSound()).vp(1f, 0.6f))),
	BRITTLE_LOW(new MFADistortion(6, 0.7f).distort(new Audio().s(SoundEnum.ENTITY_ARMORSTAND_BREAK.bukkitSound()).vp(1f, 0.1f))),
	SHOOT_HIGH(new MFADistortion(6, 1.9f).distort(new Audio().s(SoundEnum.ENTITY_SHULKER_SHOOT.bukkitSound()).vp(1f, 1f))),
	SHOOT_MID(new MFADistortion(6, 1.5f).distort(new Audio().s(SoundEnum.ENTITY_SHULKER_SHOOT.bukkitSound()).vp(1f, 0.6f))),
	SHOOT_LOW(new MFADistortion(6, 0.7f).distort(new Audio().s(SoundEnum.ENTITY_SHULKER_SHOOT.bukkitSound()).vp(1f, 0.1f))),
	TWIG_HIGH(new MFADistortion(6, 1.9f).distort(new Audio().s(SoundEnum.ENTITY_PAINTING_PLACE.bukkitSound()).vp(1f, 1f))),
	TWIG_MID(new MFADistortion(6, 1.5f).distort(new Audio().s(SoundEnum.ENTITY_PAINTING_PLACE.bukkitSound()).vp(1f, 0.6f))),
	TWIG_LOW(new MFADistortion(6, 0.7f).distort(new Audio().s(SoundEnum.ENTITY_PAINTING_PLACE.bukkitSound()).vp(1f, 0.1f))),
	WOOD_CRACK_HIGH(new MFADistortion(9, 1.9f).distort(new Audio().s(SoundEnum.ZOMBIE_WOODBREAK.bukkitSound()).vp(0.3f, 1f))),
	WOOD_CRACK_MID(new MFADistortion(9, 1.5f).distort(new Audio().s(SoundEnum.ZOMBIE_WOODBREAK.bukkitSound()).vp(0.3f, 0.6f))),
	WOOD_CRACK_LOW(new MFADistortion(9, 0.7f).distort(new Audio().s(SoundEnum.ZOMBIE_WOODBREAK.bukkitSound()).vp(0.3f, 0.1f))),
	CRUMBLE_HIGH(new MFADistortion(6, 1.9f).distort(new Audio().s(SoundEnum.BLOCK_CHORUS_FLOWER_DEATH.bukkitSound()).vp(1f, 1f))),
	CRUMBLE_MID(new MFADistortion(6, 1.5f).distort(new Audio().s(SoundEnum.BLOCK_CHORUS_FLOWER_DEATH.bukkitSound()).vp(1f, 0.6f))),
	CRUMBLE_LOW(new MFADistortion(6, 0.7f).distort(new Audio().s(SoundEnum.BLOCK_CHORUS_FLOWER_DEATH.bukkitSound()).vp(1f, 0.1f))),
	CRAWL_HIGH(new MFADistortion(6, 1.9f).distort(new Audio().s(SoundEnum.BLOCK_CHORUS_FLOWER_GROW.bukkitSound()).vp(1f, 1f))),
	CRAWL_MID(new MFADistortion(6, 1.5f).distort(new Audio().s(SoundEnum.BLOCK_CHORUS_FLOWER_GROW.bukkitSound()).vp(1f, 0.6f))),
	CRAWL_LOW(new MFADistortion(6, 0.7f).distort(new Audio().s(SoundEnum.BLOCK_CHORUS_FLOWER_GROW.bukkitSound()).vp(1f, 0.1f))),
	METAL_CLINK_HIGH(new MFADistortion(8, 1.9f).distort(new Audio().s(SoundEnum.BLOCK_IRON_TRAPDOOR_CLOSE.bukkitSound()).vp(1f, 1f))),
	METAL_CLINK_MID(new MFADistortion(8, 1.5f).distort(new Audio().s(SoundEnum.BLOCK_IRON_TRAPDOOR_CLOSE.bukkitSound()).vp(1f, 0.6f))),
	METAL_CLINK_LOW(new MFADistortion(8, 1f).distort(new Audio().s(SoundEnum.BLOCK_IRON_TRAPDOOR_CLOSE.bukkitSound()).vp(1f, 0.2f))),
	DEEP_THUMP_HIGH(new MFADistortion(8, 1.9f).distort(new Audio().s(SoundEnum.ENTITY_SHULKER_BULLET_HIT.bukkitSound()).vp(1f, 1f))),
	DEEP_THUMP_MID(new MFADistortion(8, 1.5f).distort(new Audio().s(SoundEnum.ENTITY_SHULKER_BULLET_HIT.bukkitSound()).vp(1f, 0.6f))),
	DEEP_THUMP_LOW(new MFADistortion(8, 1f).distort(new Audio().s(SoundEnum.ENTITY_SHULKER_BULLET_HIT.bukkitSound()).vp(1f, 0.2f))),
	DEEP_PUNCH_HIGH(new MFADistortion(8, 1.9f).distort(new Audio().s(SoundEnum.SUCCESSFUL_HIT.bukkitSound()).vp(1f, 1f))),
	DEEP_PUNCH_MID(new MFADistortion(8, 1.5f).distort(new Audio().s(SoundEnum.SUCCESSFUL_HIT.bukkitSound()).vp(1f, 0.6f))),
	DEEP_PUNCH_LOW(new MFADistortion(8, 0.7f).distort(new Audio().s(SoundEnum.SUCCESSFUL_HIT.bukkitSound()).vp(1f, 0.1f))),
	DEEP_CLICK_HIGH(new MFADistortion(6, 1.9f).distort(new Audio().s(SoundEnum.FIREWORK_BLAST.bukkitSound()).vp(1f, 1f))),
	DEEP_CLICK_MID(new MFADistortion(6, 1.5f).distort(new Audio().s(SoundEnum.FIREWORK_BLAST.bukkitSound()).vp(1f, 0.6f))),
	DEEP_CLICK_LOW(new MFADistortion(6, 0.7f).distort(new Audio().s(SoundEnum.FIREWORK_BLAST.bukkitSound()).vp(1f, 0.1f))),
	SHOVEL_HIGH(new MFADistortion(6, 1.9f).distort(new Audio().s(SoundEnum.ITEM_SHOVEL_FLATTEN.bukkitSound()).vp(1f, 1f))),
	SHOVEL_MID(new MFADistortion(6, 1.5f).distort(new Audio().s(SoundEnum.ITEM_SHOVEL_FLATTEN.bukkitSound()).vp(1f, 0.6f))),
	SHOVEL_LOW(new MFADistortion(6, 0.7f).distort(new Audio().s(SoundEnum.ITEM_SHOVEL_FLATTEN.bukkitSound()).vp(1f, 0.1f))),
	DEEP_BOOM_HIGH(new MFADistortion(6, 1.9f).distort(new Audio().s(SoundEnum.FIREWORK_LARGE_BLAST2.bukkitSound()).vp(1f, 1f))),
	DEEP_BOOM_MID(new MFADistortion(6, 1.5f).distort(new Audio().s(SoundEnum.FIREWORK_LARGE_BLAST2.bukkitSound()).vp(1f, 0.6f))),
	DEEP_BOOM_LOW(new MFADistortion(6, 0.7f).distort(new Audio().s(SoundEnum.FIREWORK_LARGE_BLAST2.bukkitSound()).vp(1f, 0.1f))),
	DEEP_SQUELCH_HIGH(new MFADistortion(6, 1.9f).distort(new Audio().s(SoundEnum.SLIME_WALK2.bukkitSound()).vp(1f, 1f))),
	DEEP_SQUELCH_MID(new MFADistortion(6, 1.5f).distort(new Audio().s(SoundEnum.SLIME_WALK2.bukkitSound()).vp(1f, 0.6f))),
	DEEP_SQUELCH_LOW(new MFADistortion(6, 0.7f).distort(new Audio().s(SoundEnum.SLIME_WALK2.bukkitSound()).vp(1f, 0.1f))),
	METAL_HIT_HIGH(new MFADistortion(8, 1.9f).distort(new Audio().s(SoundEnum.ZOMBIE_METAL.bukkitSound()).vp(1f, 1f))),
	METAL_HIT_MID(new MFADistortion(8, 1.5f).distort(new Audio().s(SoundEnum.ZOMBIE_METAL.bukkitSound()).vp(1f, 0.6f))),
	METAL_HIT_LOW(new MFADistortion(8, 1f).distort(new Audio().s(SoundEnum.ZOMBIE_METAL.bukkitSound()).vp(1f, 0.2f))),
	WOOD_HIT_HIGH(new MFADistortion(8, 1.9f).distort(new Audio().s(SoundEnum.ZOMBIE_WOOD.bukkitSound()).vp(1f, 1f))),
	WOOD_HIT_MID(new MFADistortion(8, 1.5f).distort(new Audio().s(SoundEnum.ZOMBIE_WOOD.bukkitSound()).vp(1f, 0.6f))),
	WOOD_HIT_LOW(new MFADistortion(8, 1f).distort(new Audio().s(SoundEnum.ZOMBIE_WOOD.bukkitSound()).vp(1f, 0.2f)));

	private Audible sound;

	private Instrument(Audible sound)
	{
		this.sound = sound;
	}

	public void play(Location l)
	{
		sound.play(l);
	}

	public void play(Player p)
	{
		sound.play(p);
	}

	public void play(Player p, Location l)
	{
		sound.play(p, l);
	}
}
