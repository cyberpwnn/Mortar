package mortar.api.fulcrum.util;

import mortar.api.fulcrum.FulcrumInstance;
import mortar.api.fulcrum.object.CustomItem;
import mortar.api.fulcrum.object.CustomModel;
import mortar.api.fulcrum.object.CustomTexture;
import mortar.api.fulcrum.resourcepack.ModelType;
import mortar.api.fulcrum.resourcepack.TextureType;

public class CustomSkin extends CustomItem
{
	public CustomSkin(String id)
	{
		super(id);
		setMaxStackSize(1);
		setItemToolLevel(0);
		setItemToolType(ToolType.HAND);
	}

	@Override
	public String getAllocatedModel()
	{
		return "inventory/" + model.getModelName();
	}

	public void setModel(DefaultSkinModel model)
	{
		setModel(model.getPath());
	}

	@Override
	public void setModel(String modelResource)
	{
		setModel(new CustomModel(getID(), FulcrumInstance.instance.getResource(modelResource), ModelType.INVENTORY));
	}

	@Override
	public void setModel(IResource modelResource)
	{
		setModel(new CustomModel(getID(), modelResource, ModelType.INVENTORY));
	}

	@Override
	public void setTexture(String name, IResource resource)
	{
		textures.put(name, new CustomTexture(name, resource, TextureType.INVENTORIES));
	}

	@Override
	public void setModel(String name, IResource resource)
	{
		model = new CustomModel(name, resource, ModelType.INVENTORY);
	}
}
