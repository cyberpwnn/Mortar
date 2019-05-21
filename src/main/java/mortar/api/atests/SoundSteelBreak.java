package mortar.api.atests;

import org.bukkit.SoundCategory;

import mortar.api.fulcrum.object.FCUSound;

public class SoundSteelBreak extends FCUSound
{
	public SoundSteelBreak()
	{
		super("steel.break");
		setCategory(SoundCategory.BLOCKS);
		setDefaultPitch(1f);
		setDefaultPitchRandomness(0.175f);
		setDefaultVolume(1f);
		setStream(false);
		setSubtitle("Steel Broken");
		addSounds("steelbreak$", getClass(), "/assets/sounds/material/metal/metalbar_walk$.ogg", 1, 7);
	}
}
