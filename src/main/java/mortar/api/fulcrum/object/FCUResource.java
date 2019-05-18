package mortar.api.fulcrum.object;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import mortar.api.fulcrum.FulcrumInstance;
import mortar.api.fulcrum.registry.FCURegisteredObject;

public class FCUResource extends FCURegisteredObject
{
	private String cacheKey;

	public FCUResource(String id, String cacheKey)
	{
		super(id);
		this.cacheKey = cacheKey;

		if(cacheKey == null)
		{
			throw new RuntimeException("Cached resource key cannot be null!");
		}
	}

	protected void updateCacheKey(String ncc)
	{
		this.cacheKey = ncc;
	}

	@SuppressWarnings("deprecation")
	public URL toURL()
	{
		try
		{
			return FulcrumInstance.instance.getResources().fileFor(cacheKey).toURL();
		}

		catch(MalformedURLException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public FCUResource(String id, Class<?> anchor, String resource)
	{
		this(id, FulcrumInstance.instance.getResources().cacheResource(anchor, resource));
	}

	public InputStream stream()
	{
		return FulcrumInstance.instance.getResources().getCachedResource(cacheKey);
	}
}
