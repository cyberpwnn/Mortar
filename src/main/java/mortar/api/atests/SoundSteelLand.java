package mortar.api.atests;

import org.bukkit.SoundCategory;

import mortar.api.fulcrum.object.FCUSound;

public class SoundSteelLand extends FCUSound
{
	public SoundSteelLand()
	{
		super("steel.land");
		setCategory(SoundCategory.BLOCKS);
		setDefaultPitch(1f);
		setDefaultPitchRandomness(0.175f);
		setDefaultVolume(0.8f);
		setStream(false);
		setSubtitle("Lands on Steel");
		addSounds("steel$", getClass(), "/assets/sounds/material/metal/metalbar_break$.ogg", 1, 11);
	}
}
