package mortar.api.fulcrum.util;

import java.io.InputStream;

public interface IResource
{
	public String getVirtualPath();

	public InputStream getInputStream();
}
