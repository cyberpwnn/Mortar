package mortar.api.fulcrum.object;

import mortar.api.resourcepack.ModelType;
import mortar.api.resourcepack.PackNode;

public class FCUModel extends FCUTextResource
{
	private ModelType modelType;

	public FCUModel(String id, String text, ModelType modelType)
	{
		super(id, text);
		this.modelType = modelType;
	}

	public FCUModel(String id, Class<?> anchor, String resource, ModelType modelType)
	{
		super(id, anchor, resource);
		this.modelType = modelType;
	}

	public ModelType getModelType()
	{
		return modelType;
	}

	public void setModelType(ModelType modelType)
	{
		this.modelType = modelType;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((modelType == null) ? 0 : modelType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
		{
			return true;
		}

		if(!super.equals(obj))
		{
			return false;
		}
		if(!(obj instanceof FCUModel))
		{
			return false;
		}
		FCUModel other = (FCUModel) obj;
		if(modelType != other.modelType)
		{
			return false;
		}
		return true;
	}

	@Override
	public String getID()
	{
		return toPackPath();
	}

	public String getModelName()
	{
		return super.getID();
	}

	public String toPackPath()
	{
		if(getModelType() != null)
		{
			return PackNode.model(getModelType(), super.getID() + ".json");
		}

		return null;
	}
}
