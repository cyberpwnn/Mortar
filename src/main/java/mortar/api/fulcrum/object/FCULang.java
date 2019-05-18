package mortar.api.fulcrum.object;

public class FCULang extends FCUTextResource
{
	private String cachedText;

	public FCULang(String id, String text)
	{
		super(id, text);
		this.cachedText = text;
	}

	public String toLine()
	{
		return getID().toLowerCase().replaceAll("\\Q_\\E", ".").trim() + "=" + cachedText;
	}
}
