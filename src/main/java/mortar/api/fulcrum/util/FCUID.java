package mortar.api.fulcrum.util;

public class FCUID
{
	private String id;

	public FCUID()
	{
		this.id = "";
	}

	public FCUID(String id)
	{
		this.id = id;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}
}
