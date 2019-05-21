package mortar.api.atests;

import org.bukkit.SoundCategory;

import mortar.api.fulcrum.object.CustomSound;

public class SoundSteelPlace extends CustomSound
{
	public SoundSteelPlace()
	{
		super("steel.place");
		setCategory(SoundCategory.BLOCKS);
		setDefaultPitch(1f);
		setDefaultPitchRandomness(0.175f);
		setDefaultVolume(1f);
		setStream(false);
		setSubtitle("Steel Placed");
		addSounds("steelbreak$", getClass(), "/assets/sounds/material/metal/metalbar_walk$.ogg", 1, 7);
	}
}
