package mortar.api.atests;

import org.bukkit.SoundCategory;

import mortar.api.fulcrum.object.FCUSound;

public class SoundSteelStep extends FCUSound
{
	public SoundSteelStep()
	{
		super("steel.step");
		setCategory(SoundCategory.BLOCKS);
		setDefaultPitch(1.15f);
		setDefaultPitchRandomness(0.175f);
		setDefaultVolume(0.3f);
		setStream(false);
		setSubtitle("Walking on Steel");
		addSounds("steel$", getClass(), "/assets/sounds/material/metal/metalbar_break$.ogg", 1, 11);
	}
}
