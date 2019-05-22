package mortar.api.fulcrum.util;

import mortar.api.inventory.WindowPosition;

public class InventorySkinElement
{
	private WindowPosition position;
	private DefaultSkinModel model;

	public InventorySkinElement(WindowPosition position, DefaultSkinModel model)
	{
		this.position = position;
		this.model = model;
	}

	public WindowPosition getPosition()
	{
		return position;
	}

	public void setPosition(WindowPosition position)
	{
		this.position = position;
	}

	public DefaultSkinModel getModel()
	{
		return model;
	}

	public void setModel(DefaultSkinModel model)
	{
		this.model = model;
	}
}
