package mortar.api.tome;

import java.util.Iterator;

import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.Text;

public class TomeParagraph extends TomeText
{
	@Override
	public void read(Node thisElement)
	{
		Element e = (Element) thisElement;

		for(Iterator<Node> it = e.nodeIterator(); it.hasNext();)
		{
			Node i = it.next();

			if(i instanceof Element)
			{
				TomeComponent component = null;

				if(i.getName().equals("f"))
				{
					component = new TomeFormat();
				}

				else if(i.getName().equals("anchor"))
				{
					component = new TomeAnchor();
				}

				else if(i.getName().equals("keybind"))
				{
					component = new TomeKeybind();
				}

				if(component != null)
				{
					component.read(i);
					add(component);
				}
			}

			else if(i instanceof Text)
			{
				add(((Text) i).getText());
			}
		}
	}

	@Override
	public void construct(Element parent)
	{
		Element p = parent.addElement("p");

		for(TomeComponent i : getComponents())
		{
			i.construct(p);
		}
	}
}
