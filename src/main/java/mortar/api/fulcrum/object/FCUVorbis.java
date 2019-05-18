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
}
