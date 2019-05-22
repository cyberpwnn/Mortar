package mortar.api.fulcrum.object;

import org.bukkit.entity.Player;

import mortar.api.fulcrum.FulcrumRegistry;
import mortar.api.fulcrum.registry.FCURegisteredObject;
import mortar.api.fulcrum.util.CustomSkin;
import mortar.api.fulcrum.util.InventorySkinElement;
import mortar.api.fulcrum.util.InventorySkinType;
import mortar.api.inventory.UIElement;
import mortar.api.inventory.UIWindow;
import mortar.api.inventory.Window;
import mortar.api.inventory.WindowPosition;
import mortar.api.inventory.WindowResolution;
import mortar.lang.collection.GList;
import mortar.lang.collection.GMap;
import mortar.logic.format.F;

public class CustomInventory extends FCURegisteredObject
{
	private String name;
	private InventorySkinType skinType;
	private GMap<WindowPosition, CustomSkin> skin;
	private String skinTexture;

	public CustomInventory(String id)
	{
		super(id);
		skin = new GMap<>();
		setSkinType(InventorySkinType.W9_H3);
		setSkinTexture("/assets/textures/inventories/chest_3/layout.png");
		setName(getFancyNameFromID());
	}

	public WindowResolution getWindowResolution()
	{
		return getSkinType().getResolution();
	}

	public GList<InventorySkinElement> getSkinElements()
	{
		return getSkinType().getSkinElements();
	}

	public void registerResources(FulcrumRegistry registry)
	{
		skin.clear();

		for(InventorySkinElement i : getSkinElements())
		{
			CustomSkin skin = new CustomSkin(getID() + "_" + i.getPosition().getPosition() + "_" + i.getPosition().getRow());
			skin.setModel(i.getModel());
			skin.getModel().rewrite("$id", skin.getID());
			skin.setTexture(skin.getID(), getSkinTexture());
			skin.setName(getName());
			registry.skin().register(skin);
			this.skin.put(i.getPosition(), skin);
		}
	}

	public String getSkinTexture()
	{
		return skinTexture;
	}

	public void setSkinTexture(String skinTexture)
	{
		this.skinTexture = skinTexture;
	}

	public InventorySkinType getSkinType()
	{
		return skinType;
	}

	public String getFancyNameFromID()
	{
		return F.capitalizeWords(getID().replaceAll("\\Q_\\E", " "));
	}

	public void setSkinType(InventorySkinType skinType)
	{
		this.skinType = skinType;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void showWindow(Player player)
	{
		//@builder
		Window w = new UIWindow(player)
				.setResolution(getWindowResolution())
				.setTitle("You Shouldnt See Me");
		//@done

		for(WindowPosition i : skin.k())
		{
			//@builder
			w.setElement(i.getPosition(), i.getRow(), new UIElement(skin.get(i).getID())
					.setItemStack(skin.get(i).toItemStack(1)));
			//@done
		}

		w.open();
	}
}
