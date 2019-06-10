package mortar.api.tome;

import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.Text;

public class TomeFormat extends TomeComponent
{
	private String color;
	private String format;
	private String onClick;

	public TomeFormat()
	{

	}

	@Override
	public void read(Node thisElement)
	{
		Element e = (Element) thisElement;

		for(Attribute i : e.attributes())
		{
			if(i.getName().equals("color"))
			{
				setColor(i.getStringValue());
			}

			if(i.getName().equals("format"))
			{
				setFormat(i.getStringValue());
			}

			if(i.getName().equals("click"))
			{
				setOnClick(i.getStringValue());
			}
		}

		for(Iterator<Node> it = e.nodeIterator(); it.hasNext();)
		{
			Node i = it.next();

			if(i instanceof Element)
			{
				TomeComponent component = null;

				if(i.getName().equals("hover"))
				{
					component = new TomeHover();
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
		Element f = parent.addElement("f");

		if(getColor() != null)
		{
			f.addAttribute("color", getColor());
		}

		if(getFormat() != null)
		{
			f.addAttribute("format", getFormat());
		}

		if(getOnClick() != null)
		{
			f.addAttribute("click", getOnClick());
		}

		for(TomeComponent i : getComponents())
		{
			i.construct(f);
		}
	}

	public String getColor()
	{
		return color;
	}

	public TomeFormat setColor(String color)
	{
		this.color = color;
		return this;
	}

	public String getFormat()
	{
		return format;
	}

	public TomeFormat setFormat(String format)
	{
		this.format = format;
		return this;
	}

	public String getOnClick()
	{
		return onClick;
	}

	public TomeFormat setOnClick(String onClick)
	{
		this.onClick = onClick;
		return this;
	}
}
