package mortar.api.fulcrum.object;

public class FCUVorbis extends FCUResource
{
	public FCUVorbis(String id, String cacheKey)
	{
		super(id, cacheKey);
	}

	public FCUVorbis(String id, Class<?> anchor, String resource)
	{
		super(id, anchor, resource);
	}

	public String toPackPath()
	{
		return "sounds/" + getID() + ".ogg";
	}

	public String toSoundsPath()
	{
		return toSoundsPathJ() + ".ogg";
	}

	public String toSoundsPathJ()
	{
		return getID();
	}
}
