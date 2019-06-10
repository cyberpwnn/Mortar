package mortar.api.tome;

import org.dom4j.Element;
import org.dom4j.Node;

import mortar.lang.collection.GList;

public class TomeComponent implements WritableComponent, ReadableComponent
{
	private GList<TomeComponent> components;

	public TomeComponent()
	{
		this.components = new GList<>();
	}

	@Override
	public void read(Node thisElement)
	{

	}

	@Override
	public void construct(Element parent)
	{
		for(TomeComponent i : getComponents())
		{
			i.construct(parent);
		}
	}

	public GList<TomeComponent> getComponents()
	{
		return components;
	}

	public void setComponents(GList<TomeComponent> components)
	{
		this.components = components;
	}

	public void clearComponents()
	{
		getComponents();
	}

	public TomeComponent add(TomeComponent component)
	{
		getComponents().add(component);
		return this;
	}

	public TomeComponent add(String component)
	{
		getComponents().add(new TomeText(component));
		return this;
	}
}
