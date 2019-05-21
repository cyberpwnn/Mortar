package mortar.api.fulcrum.resourcepack;

import java.net.URL;

import mortar.bukkit.plugin.Mortar;
import mortar.lang.json.JSONObject;

public class PackMeta
{
	private String packDescription;
	private int packFormat;
	private URL packIcon;

	public PackMeta(String packDescription, int packFormat, URL packIcon)
	{
		this.packDescription = packDescription;
		this.packFormat = packFormat;
		this.packIcon = packIcon;
	}

	public PackMeta(String packDescription, int packFormat)
	{
		this(packDescription, packFormat, Mortar.class.getResource("/assets/textures/blocks/unknown.png"));
	}

	public PackMeta(String packDescription)
	{
		this(packDescription, 3);
	}

	public PackMeta(int packFormat)
	{
		this("No Description", packFormat);
	}

	public PackMeta()
	{
		this(3);
	}

	public String getPackDescription()
	{
		return packDescription;
	}

	public void setPackDescription(String packDescription)
	{
		this.packDescription = packDescription;
	}

	public int getPackFormat()
	{
		return packFormat;
	}

	public void setPackFormat(int packFormat)
	{
		this.packFormat = packFormat;
	}

	public URL getPackIcon()
	{
		return packIcon;
	}

	public void setPackIcon(URL packIcon)
	{
		this.packIcon = packIcon;
	}

	@Override
	public String toString()
	{
		JSONObject j = new JSONObject();
		JSONObject pack = new JSONObject();
		pack.put("pack_format", getPackFormat());
		pack.put("description", getPackDescription());
		j.put("pack", pack);
		return j.toString();
	}
}
