package mortar.api.scm;

import org.bukkit.util.Vector;

public class SCMProperties
{
	private Vector size;
	private Vector origin;

	public SCMProperties(Vector size, Vector origin)
	{
		this.size = size;
		this.origin = origin;
	}

	public Vector getSize()
	{
		return size;
	}

	public Vector getOrigin()
	{
		return origin;
	}
}
