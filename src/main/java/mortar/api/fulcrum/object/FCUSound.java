package mortar.api.fulcrum.object;

import org.bukkit.SoundCategory;

import mortar.api.fulcrum.FulcrumRegistry;
import mortar.api.fulcrum.registry.FCURegisteredObject;
import mortar.api.sound.Audible;
import mortar.api.sound.Audio;
import mortar.lang.collection.GList;
import mortar.lang.json.JSONArray;
import mortar.lang.json.JSONObject;

public class FCUSound extends FCURegisteredObject
{
	private float defaultVolume;
	private float defaultPitch;
	private float defaultPitchRandomness;
	private String subtitle;
	private SoundCategory category;
	private boolean stream;
	private GList<FCUVorbis> sounds;

	public FCUSound(String id)
	{
		super(id);
		sounds = new GList<>();
		setSubtitle("No Subtitle");
		setCategory(SoundCategory.MASTER);
		setStream(false);
	}

	public void addSounds(String id, Class<?> anchor, String path, int from, int to)
	{
		for(int i = from; i <= to; i++)
		{
			addSound(id.replaceAll("\\Q$\\E", i + ""), anchor, path.replaceAll("\\Q$\\E", i + ""));
		}
	}

	public void addSound(String id, Class<?> anchor, String path)
	{
		sounds.add(new FCUVorbis(id, anchor, path));
	}

	public float getDefaultVolume()
	{
		return defaultVolume;
	}

	public void setDefaultVolume(float defaultVolume)
	{
		this.defaultVolume = defaultVolume;
	}

	public float getDefaultPitch()
	{
		return defaultPitch;
	}

	public void setDefaultPitch(float defaultPitch)
	{
		this.defaultPitch = defaultPitch;
	}

	public String getSubtitle()
	{
		return subtitle;
	}

	public void setSubtitle(String subtitle)
	{
		this.subtitle = subtitle;
	}

	public SoundCategory getCategory()
	{
		return category;
	}

	public void setCategory(SoundCategory category)
	{
		this.category = category;
	}

	public boolean isStream()
	{
		return stream;
	}

	public void setStream(boolean stream)
	{
		this.stream = stream;
	}

	public void registerResources(FulcrumRegistry registry)
	{
		registry.lang().register(new FCULang(getLocalizedName(), getSubtitle()));

		for(FCUVorbis i : getSounds())
		{
			registry.vorbis().register(i);
		}
	}

	public void addToJSON(JSONObject master)
	{
		JSONObject j = new JSONObject();
		j.put("subtitle", getLocalizedName());
		j.put("stream", isStream());
		j.put("category", getCategory().name().toLowerCase());

		JSONArray ja = new JSONArray();

		for(FCUVorbis i : getSounds())
		{
			ja.put(i.toSoundsPathJ());
		}

		j.put("sounds", ja);

		master.put(getSoundID(), j);
	}

	public Audible constructAudible()
	{
		//@builder
		return new Audio()
				.c(getCategory())
				.p((float) (getDefaultPitch() + (((Math.random() *2D) - 1D) * getDefaultPitchRandomness())))
				.v(getDefaultVolume())
				.s(getSoundID());
		//@done
	}

	public String getSoundID()
	{
		return getID().replaceAll("\\Q_\\E", ".");
	}

	public String getLocalizedName()
	{
		return ("fcu.category." + getID() + ".name");
	}

	public GList<FCUVorbis> getSounds()
	{
		return sounds;
	}

	public float getDefaultPitchRandomness()
	{
		return defaultPitchRandomness;
	}

	public void setDefaultPitchRandomness(float defaultPitchRandomness)
	{
		this.defaultPitchRandomness = defaultPitchRandomness;
	}
}
