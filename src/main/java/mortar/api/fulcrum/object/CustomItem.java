package mortar.api.fulcrum.object;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import mortar.api.fulcrum.FulcrumInstance;
import mortar.api.fulcrum.FulcrumRegistry;
import mortar.api.fulcrum.resourcepack.ModelType;
import mortar.api.fulcrum.resourcepack.TextureType;
import mortar.api.fulcrum.util.DefaultItemModel;
import mortar.api.fulcrum.util.IResource;
import mortar.api.fulcrum.util.ToolLevel;
import mortar.api.fulcrum.util.ToolType;
import mortar.lang.collection.GMap;
import mortar.logic.format.F;

public class CustomItem extends CustomCollective
{
	protected GMap<String, CustomTexture> textures;
	protected CustomModel model;
	private String name;
	private int maxStackSize;
	private String itemToolType;
	private int itemToolLevel;

	public CustomItem(String id)
	{
		super(id);
		textures = new GMap<>();
		setTexture(id, "assets/textures/items/unknown.png");
		setModel(DefaultItemModel.ITEM);
		model.rewrite("$id", id);
		setName(getFancyNameFromID());
		setMaxStackSize(64);
		setItemToolType(ToolType.HAND);
		setItemToolLevel(ToolLevel.HAND);
	}

	public void setTexture(String id, String r)
	{
		setTexture(id, FulcrumInstance.instance.getResource(r));
	}

	public int getItemToolLevel()
	{
		return itemToolLevel;
	}

	public void setItemToolLevel(int itemToolLevel)
	{
		this.itemToolLevel = itemToolLevel;
	}

	public String getItemToolType()
	{
		return itemToolType;
	}

	public void setItemToolType(String itemToolType)
	{
		this.itemToolType = itemToolType;
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

	public void setTexture(String name, IResource resource)
	{
		textures.put(name, new CustomTexture(name, resource, TextureType.ITEMS));
	}

	public void setModel(String name, IResource resource)
	{
		model = new CustomModel(name, resource, ModelType.ITEM);
	}

	@Override
	public void registerResources(FulcrumRegistry registry)
	{
		registry.model().register(model);
		registry.lang().register(new CustomLang(getLocalizedName(), getName()));

		for(CustomTexture i : textures.v())
		{
			registry.texture().register(i);
		}
	}

	public String getLocalizedName()
	{
		return ("fcu." + getID() + ".name");
	}

	public GMap<String, CustomTexture> getTextures()
	{
		return textures;
	}

	public void setTextures(GMap<String, CustomTexture> textures)
	{
		this.textures = textures;
	}

	public CustomModel getModel()
	{
		return model;
	}

	public void setModel(CustomModel model)
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
	public CustomBlock block()
	{
		return (CustomBlock) this;
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

	public void setModel(DefaultItemModel model)
	{
		setModel(model.getPath());
	}

	public void setModel(String modelResource)
	{
		setModel(new CustomModel(getID(), FulcrumInstance.instance.getResource(modelResource), ModelType.ITEM));
	}

	public void setModel(IResource modelResource)
	{
		setModel(new CustomModel(getID(), modelResource, ModelType.ITEM));
	}
}
