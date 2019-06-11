package mortar.api.tome;

import org.dom4j.Element;
import org.dom4j.Node;

public class TomeKeybind extends TomeComponent
{
	private String key;

	public TomeKeybind()
	{
		this("");
	}

	@Override
	public void read(Node thisElement)
	{
		Element e = (Element) thisElement;
		key = e.attributeValue("key");
	}

	@Override
	public void construct(Element parent)
	{
		Element anchor = parent.addElement("keybind");
		anchor.addAttribute("key", getKeyName());
	}

	public TomeKeybind(String key)
	{
		this.key = key;
	}

	public String getKeyName()
	{
		return key;
	}

	public void setKeyName(String key)
	{
		this.key = key;
	}
}
