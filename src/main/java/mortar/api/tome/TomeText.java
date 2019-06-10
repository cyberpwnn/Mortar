package mortar.api.tome;

import org.dom4j.Element;
import org.dom4j.Node;

public class TomeText extends TomeComponent
{
	private String text;

	public TomeText()
	{
		this("");
	}

	@Override
	public void read(Node thisElement)
	{
		text = thisElement.getText();
	}

	@Override
	public void construct(Element parent)
	{
		parent.addText(getText());
	}

	public TomeText(String text)
	{
		this.text = text;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}
}
