package mortar.api.fulcrum.object;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import mortar.api.fulcrum.FulcrumInstance;
import mortar.api.fulcrum.registry.FCURegisteredObject;
import mortar.api.fulcrum.util.IResource;

public class CustomResource extends FCURegisteredObject
{
	private String cacheKey;

	public CustomResource(String id, String cacheKey)
	{
		super(id);
		this.cacheKey = cacheKey;

		if(cacheKey == null)
		{
			throw new RuntimeException("Cached resource key cannot be null!");
		}
	}

	public CustomResource(String id, IResource resource)
	{
		this(id, FulcrumInstance.instance.getResources().cacheResource(resource));
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

	public InputStream stream()
	{
		return FulcrumInstance.instance.getResources().getCachedResource(cacheKey);
	}
}
