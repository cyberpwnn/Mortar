package mortar.api.fulcrum.object;

import mortar.api.fulcrum.util.IResource;

public class CustomVorbis extends CustomResource
{
	public CustomVorbis(String id, String cacheKey)
	{
		super(id, cacheKey);
	}

	public CustomVorbis(String id, IResource resource)
	{
		super(id, resource);
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
