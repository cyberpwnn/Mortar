package mortar.api.tome;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Node;

public class TomeMeta extends TomeComponent
{
	private String property;
	private String value;

	public TomeMeta()
	{
		this("", "");
	}

	public TomeMeta(String property, String value)
	{
		this.property = property;
		this.value = value;
	}

	@Override
	public void read(Node thisElement)
	{
		Element e = (Element) thisElement;

		for(Attribute i : e.attributes())
		{
			if(i.getName().equals("property"))
			{
				setProperty(i.getStringValue());
			}
		}

		setValue(e.getText());
	}

	@Override
	public void construct(Element parent)
	{
		Element section = parent.addElement("meta");
		section.addAttribute("property", getProperty());
		section.setText(getValue());
	}

	public String getProperty()
	{
		return property;
	}

	public void setProperty(String property)
	{
		this.property = property;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

}
