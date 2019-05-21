package mortar.api.fulcrum.object;

public class CustomLang extends CustomTextResource
{
	private String cachedText;

	public CustomLang(String id, String text)
	{
		super(id, text);
		this.cachedText = text;
	}

	public String toLine()
	{
		return getID().toLowerCase().trim() + "=" + cachedText;
	}
}
