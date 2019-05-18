package mortar.api.fulcrum.object;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import mortar.api.fulcrum.FulcrumRegistry;
import mortar.api.resourcepack.ModelType;
import mortar.api.resourcepack.TextureType;
import mortar.lang.collection.GMap;
import mortar.logic.format.F;

public class FCUItem extends FCUCollective
{
	protected GMap<String, FCUTexture> textures;
	protected FCUModel model;
	private String name;
	private int maxStackSize;

	public FCUItem(String id)
	{
		super(id);
		textures = new GMap<>();
		setTexture(id, "assets/textures/items/unknown.png");
		setModel(id, "assets/models/item/default_item.json");
		model.rewrite("$texture", id);
		setName(getFancyNameFromID());
		setMaxStackSize(64);
	}

	public ItemStack toItemStack(int amt)
	{
		ItemStack is = new ItemStack(getAllocationMaterial(), amt, getAllocationID());
		ItemMeta im = is.getItemMeta();
		im.setUnbreakable(true);
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		im.addItemFlags(ItemFlag.HIDE_PLACED_ON);
		im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		im.setLocalizedName(getLocalizedName());
		is.setItemMeta(im);

		return is;
	}

	public String getFancyNameFromID()
	{
		return F.capitalizeWords(getID().replaceAll("\\Q_\\E", " "));
	}

	public void clearTextures()
	{
		textures.clear();
	}

	public void setTexture(String name, String resource)
	{
		textures.put(name, new FCUTexture(name, getClass(), resource, TextureType.ITEMS));
	}

	public void setModel(String name, String resource)
	{
		model = new FCUModel(name, getClass(), resource, ModelType.ITEM);
	}

	@Override
	public void registerResources(FulcrumRegistry registry)
	{
		registry.model().register(model);
		registry.lang().register(new FCULang(getLocalizedName(), getName()));

		for(FCUTexture i : textures.v())
		{
			registry.texture().register(i);
		}
	}

	public String getLocalizedName()
	{
		return "fcu." + getID() + ".name";
	}

	public GMap<String, FCUTexture> getTextures()
	{
		return textures;
	}

	public void setTextures(GMap<String, FCUTexture> textures)
	{
		this.textures = textures;
	}

	public FCUModel getModel()
	{
		return model;
	}

	public void setModel(FCUModel model)
	{
		this.model = model;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public String getAllocatedModel()
	{
		return "item/" + model.getModelName();
	}

	@Override
	public FCUBlock block()
	{
		return (FCUBlock) this;
	}

	@Override
	public boolean isBlock()
	{
		return false;
	}

	@Override
	public boolean isItem()
	{
		return true;
	}

	public void setMaxStackSize(int maxStackSize)
	{
		this.maxStackSize = maxStackSize;
	}

	public int getMaxStackSize()
	{
		return maxStackSize;
	}
}
