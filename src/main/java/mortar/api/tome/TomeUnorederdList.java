package mortar.api.tome;

import java.util.Iterator;

import org.dom4j.Element;
import org.dom4j.Node;

public class TomeUnorederdList extends TomeComponent
{
	public TomeUnorederdList()
	{

	}

	@Override
	public void read(Node thisElement)
	{
		Element e = (Element) thisElement;

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
		Element ul = parent.addElement("ul");

		for(TomeComponent i : getComponents())
		{
			i.construct(ul);
		}
	}
}
