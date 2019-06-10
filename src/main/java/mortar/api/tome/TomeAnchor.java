package mortar.api.tome;

import org.dom4j.Element;
import org.dom4j.Node;

public class TomeAnchor extends TomeComponent
{
	private String name;

	public TomeAnchor()
	{
		this("");
	}

	@Override
	public void read(Node thisElement)
	{
		Element e = (Element) thisElement;
		name = e.attributeValue("name");
	}

	@Override
	public void construct(Element parent)
	{
		Element anchor = parent.addElement("anchor");
		anchor.addAttribute("name", getAnchorName());
	}

	public TomeAnchor(String name)
	{
		this.name = name;
	}

	public String getAnchorName()
	{
		return name;
	}

	public void setAnchorName(String name)
	{
		this.name = name;
	}
}
