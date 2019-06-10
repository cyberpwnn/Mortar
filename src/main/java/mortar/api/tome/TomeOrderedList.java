package mortar.api.tome;

import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Node;

public class TomeOrderedList extends TomeComponent
{
	private String type;

	public TomeOrderedList()
	{
		type = "normal";
	}

	public TomeOrderedList(String type)
	{
		this.type = type;
	}

	@Override
	public void read(Node thisElement)
	{
		Element e = (Element) thisElement;

		for(Attribute i : e.attributes())
		{
			if(i.getName().equals("type"))
			{
				type = i.getStringValue();
			}
		}

		for(Iterator<Element> it = e.elementIterator(); it.hasNext();)
		{
			Element i = it.next();
			TomeComponent component = null;

			if(i.getName().equals("p"))
			{
				component = new TomeParagraph();
			}

			else if(i.getName().equals("anchor"))
			{
				component = new TomeAnchor();
			}

			if(component != null)
			{
				component.read(i);
				add(component);
			}
		}
	}

	@Override
	public void construct(Element parent)
	{
		Element ol = parent.addElement("ol");

		if(!type.equals("normal"))
		{
			ol.addAttribute("type", getType());
		}

		for(TomeComponent i : getComponents())
		{
			i.construct(ol);
		}
	}

	public String getType()
	{
		return type;
	}

	public TomeOrderedList setType(String type)
	{
		this.type = type;
		return this;
	}
}
