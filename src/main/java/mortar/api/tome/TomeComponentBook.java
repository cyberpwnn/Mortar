package mortar.api.tome;

import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

public class TomeComponentBook extends TomeComponent implements WritableRoot
{
	private String name;
	private String author;
	private String volume;
	private String id;

	public TomeComponentBook()
	{
		name = "";
		author = "";
		volume = "";
		id = "";
	}

	@Override
	public void read(Node d)
	{
		Document doc = (Document) d;
		Element e = (Element) doc.getRootElement();

		for(Attribute i : e.attributes())
		{
			if(i.getName().equals("name"))
			{
				setName(i.getStringValue());
			}

			if(i.getName().equals("author"))
			{
				setAuthor(i.getStringValue());
			}

			if(i.getName().equals("volume"))
			{
				setVolume(i.getStringValue());
			}

			if(i.getName().equals("id"))
			{
				setId(i.getStringValue());
			}
		}

		for(Iterator<Element> it = e.elementIterator("meta"); it.hasNext();)
		{
			TomeMeta meta = new TomeMeta();
			meta.read(it.next());
			add(meta);
		}

		for(Iterator<Element> it = e.elementIterator("section"); it.hasNext();)
		{
			TomeSection section = new TomeSection();
			section.read(it.next());
			add(section);
		}
	}

	@Override
	public void construct(Document parent)
	{
		Element book = parent.addElement("book");
		book.addAttribute("name", getName());
		book.addAttribute("author", getAuthor());
		book.addAttribute("volume", getVolume());
		book.addAttribute("id", getId());

		for(TomeComponent i : getComponents())
		{
			i.construct(book);
		}
	}

	public String getName()
	{
		return name;
	}

	public TomeComponentBook setName(String name)
	{
		this.name = name;
		return this;
	}

	public String getAuthor()
	{
		return author;
	}

	public TomeComponentBook setAuthor(String author)
	{
		this.author = author;
		return this;
	}

	public String getVolume()
	{
		return volume;
	}

	public TomeComponentBook setVolume(String volume)
	{
		this.volume = volume;
		return this;
	}

	public String getId()
	{
		return id;
	}

	public TomeComponentBook setId(String id)
	{
		this.id = id;
		return this;
	}
}
