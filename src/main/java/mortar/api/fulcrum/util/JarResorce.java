package mortar.api.fulcrum.util;

import java.io.InputStream;

public class JarResorce implements IResource
{
	private final Class<?> anchor;
	private final String path;

	public JarResorce(Class<?> anchor, String path)
	{
		this.anchor = anchor;
		this.path = path;
	}

	@Override
	public InputStream getInputStream()
	{
		return anchor.getResourceAsStream(path);
	}

	@Override
	public String getVirtualPath()
	{
		if(path.startsWith("/"))
		{
			return path.substring(1);
		}

		return path;
	}
}
