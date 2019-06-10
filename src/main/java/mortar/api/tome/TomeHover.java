package mortar.api.tome;

import java.util.Iterator;

import org.dom4j.Element;
import org.dom4j.Node;

public class TomeHover extends TomeComponent
{
	@Override
	public void construct(Element parent)
	{
		Element hover = parent.addElement("hover");

		for(TomeComponent i : getComponents())
		{
			i.construct(hover);
		}
	}

	@Override
	public void read(Node thisElement)
	{
		Element e = (Element) thisElement;

		for(Iterator<Element> it = e.elementIterator(); it.hasNext();)
		{
			Element i = it.next();
			TomeComponent component = null;

			if(i.getName().equals("header"))
			{
				component = new TomeHeader();
			}

			else if(i.getName().equals("p"))
			{
				component = new TomeParagraph();
			}

			else if(i.getName().equals("ul"))
			{
				component = new TomeUnorederdList();
			}

			else if(i.getName().equals("ol"))
			{
				component = new TomeOrderedList();
			}

			if(component != null)
			{
				component.read(i);
				add(component);
			}
		}
	}
}
