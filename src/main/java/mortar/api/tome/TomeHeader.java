package mortar.api.tome;

import org.dom4j.Element;

public class TomeHeader extends TomeParagraph
{
	@Override
	public void construct(Element parent)
	{
		Element p = parent.addElement("header");

		for(TomeComponent i : getComponents())
		{
			i.construct(p);
		}
	}
}
