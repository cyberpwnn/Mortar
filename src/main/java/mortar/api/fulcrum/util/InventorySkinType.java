package mortar.api.fulcrum.util;

import mortar.api.inventory.WindowPosition;
import mortar.api.inventory.WindowResolution;
import mortar.lang.collection.GList;

public enum InventorySkinType
{
	//@builder
	W9_H3(WindowResolution.W9_H3,
			new InventorySkinElement(new WindowPosition(-4, 0), DefaultSkinModel.C3)),
	W9_H6(WindowResolution.W9_H6,
			new InventorySkinElement(new WindowPosition(-4, 0), DefaultSkinModel.C6_TOP),
			new InventorySkinElement(new WindowPosition(-4, 5), DefaultSkinModel.C6_BOTTOM)),
	W3_H3(WindowResolution.W3_H3),
	W3_H3_RING(WindowResolution.W3_H3),
	W5_H1(WindowResolution.W5_H1),
	W5_H1_CENTERED(WindowResolution.W5_H1);
	//@done

	private WindowResolution resolution;
	private GList<InventorySkinElement> skinElements;

	private InventorySkinType(WindowResolution resolution, InventorySkinElement... elements)
	{
		this.resolution = resolution;
		skinElements = new GList<>(elements);
	}

	public WindowResolution getResolution()
	{
		return resolution;
	}

	public GList<InventorySkinElement> getSkinElements()
	{
		return skinElements;
	}
}
