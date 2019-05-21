package mortar.api.fulcrum.object;

import mortar.api.resourcepack.PackNode;
import mortar.api.resourcepack.TextureSubSubType;
import mortar.api.resourcepack.TextureSubType;
import mortar.api.resourcepack.TextureType;

public class CustomTexture extends CustomResource
{
	private TextureType type;
	private TextureSubType subType;
	private TextureSubSubType subSubType;

	public CustomTexture(String id, String cacheKey, TextureType type, TextureSubType subType, TextureSubSubType subSubType)
	{
		super(id, cacheKey);
		this.type = type;
		this.subType = subType;
		this.subSubType = subSubType;
	}

	public CustomTexture(String id, Class<?> anchor, String resource, TextureType type, TextureSubType subType, TextureSubSubType subSubType)
	{
		super(id, anchor, resource);
		this.type = type;
		this.subType = subType;
		this.subSubType = subSubType;
	}

	public CustomTexture(String id, String cacheKey, TextureType type, TextureSubType subType)
	{
		super(id, cacheKey);
		this.type = type;
		this.subType = subType;
	}

	public CustomTexture(String id, Class<?> anchor, String resource, TextureType type, TextureSubType subType)
	{
		super(id, anchor, resource);
		this.type = type;
		this.subType = subType;
	}

	public CustomTexture(String id, String cacheKey, TextureType type)
	{
		super(id, cacheKey);
		this.type = type;
	}

	public CustomTexture(String id, Class<?> anchor, String resource, TextureType type)
	{
		super(id, anchor, resource);
		this.type = type;
	}

	public TextureType getType()
	{
		return type;
	}

	public void setType(TextureType type)
	{
		this.type = type;
	}

	public TextureSubType getSubType()
	{
		return subType;
	}

	public void setSubType(TextureSubType subType)
	{
		this.subType = subType;
	}

	public TextureSubSubType getSubSubType()
	{
		return subSubType;
	}

	public void setSubSubType(TextureSubSubType subSubType)
	{
		this.subSubType = subSubType;
	}

	@Override
	public String getID()
	{
		return toPackPath();
	}

	public String toPackPath()
	{
		if(type != null)
		{
			if(subType != null)
			{
				if(subSubType != null)
				{
					return PackNode.texture(getType(), getSubType(), getSubSubType(), super.getID() + ".png");
				}

				return PackNode.texture(getType(), getSubType(), super.getID() + ".png");
			}

			return PackNode.texture(getType(), super.getID() + ".png");
		}

		return null;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((subSubType == null) ? 0 : subSubType.hashCode());
		result = prime * result + ((subType == null) ? 0 : subType.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		if(!(obj instanceof CustomTexture))
		{
			return false;
		}
		CustomTexture other = (CustomTexture) obj;
		if(subSubType != other.subSubType)
		{
			return false;
		}
		if(subType != other.subType)
		{
			return false;
		}
		if(type != other.type)
		{
			return false;
		}
		return true;
	}
}
