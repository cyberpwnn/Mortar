package com.volmit.fulcrum.resourcepack;

public enum PackNode
{
	ADVANCEMENTS,
	BLOCKSTATES,
	FONT,
	LANG,
	LOOT_TABLES,
	MODELS,
	RECIPES,
	SHADERS,
	STRUCTURES,
	TEXTS,
	TEXTURES;

	public static String advancement(AdvancementTab tab, String name)
	{
		return "advancements/" + tab.toString().toLowerCase() + "/" + name;
	}

	public static String blockState(String name)
	{
		return "blockstates/" + name;
	}

	public static String font(String name)
	{
		return "font/" + name;
	}

	public static String lang(String name)
	{
		return "lang/" + name;
	}

	public static String lootTable(LootTableType type, String name)
	{
		return "loot_tables/" + type.toString().toLowerCase() + "/" + name;
	}

	public static String model(ModelType type, String name)
	{
		return "models/" + type.toString().toLowerCase() + "/" + name;
	}

	public static String recipe(String name)
	{
		return "recipes/" + name;
	}

	public static String text(String name)
	{
		return "texts/" + name;
	}

	public static String texture(TextureType type, String name)
	{
		return "textures/" + type.toString().toLowerCase() + "/" + name;
	}

	public static String texture(TextureType type, TextureSubType type2, String name)
	{
		return "textures/" + type.toString().toLowerCase() + "/" + type2.toString().toLowerCase() + "/" + name;
	}

	public static String texture(TextureType type, TextureSubType type2, TextureSubSubType type3, String name)
	{
		return "textures/" + type.toString().toLowerCase() + "/" + type2.toString().toLowerCase() + "/" + type2.toString().toLowerCase() + "/" + name;
	}

	public static String shader(ShaderType type, String name)
	{
		return "shaders/" + type.toString().toLowerCase() + "/" + name;
	}

	public static String structure(StructureType type, String name)
	{
		return "structures/" + type.toString().toLowerCase() + "/" + name;
	}
}
