package mortar.api.fulcrum;

import java.io.IOException;

import mortar.api.fulcrum.object.CustomItem;
import mortar.api.fulcrum.object.CustomLang;
import mortar.api.fulcrum.object.CustomModel;
import mortar.api.fulcrum.object.CustomSound;
import mortar.api.fulcrum.object.CustomTexture;
import mortar.api.fulcrum.object.CustomVorbis;
import mortar.api.fulcrum.registry.FCURegistrar;
import mortar.api.fulcrum.resourcepack.ModelType;
import mortar.api.fulcrum.resourcepack.ResourcePack;
import mortar.api.fulcrum.util.AllocationBlock;
import mortar.bukkit.plugin.Mortar;
import mortar.lang.json.JSONException;
import mortar.lang.json.JSONObject;
import mortar.logic.io.VIO;

public class FulcrumRegistry
{
	private final FCURegistrar<CustomItem> items;
	private final FCURegistrar<CustomTexture> textures;
	private final FCURegistrar<CustomModel> models;
	private final FCURegistrar<CustomSound> sounds;
	private final FCURegistrar<CustomVorbis> vorbis;
	private final FCURegistrar<CustomLang> lang;
	private final StringBuilder langBuild;
	private final AllocationBlock allocator;
	private final JSONObject soundsJSON;

	public FulcrumRegistry() throws JSONException, IOException
	{
		allocator = new AllocationBlock(Fulcrum.allocationStrategy);
		allocator.addDefaultUnits();
		langBuild = new StringBuilder();
		soundsJSON = new JSONObject(VIO.readAll(getClass().getResourceAsStream("/assets/sounds-default.json")));

		lang = new FCURegistrar<CustomLang>()
		{
			@Override
			public void onRegister(CustomLang r)
			{
				langBuild.append(r.toLine() + "\n");
			}
		};

		items = new FCURegistrar<CustomItem>()
		{
			@Override
			public void onRegister(CustomItem r)
			{
				allocator.allocate(r);
				r.registerResources(FulcrumRegistry.this);
			}
		};

		textures = new FCURegistrar<CustomTexture>()
		{
			@Override
			public void onRegister(CustomTexture r)
			{
				getPack().setResource(r.toPackPath(), r.toURL());
			}
		};

		vorbis = new FCURegistrar<CustomVorbis>()
		{
			@Override
			public void onRegister(CustomVorbis r)
			{
				getPack().setResource(r.toPackPath(), r.toURL());
			}
		};

		models = new FCURegistrar<CustomModel>()
		{
			@Override
			public void onRegister(CustomModel r)
			{
				getPack().setResource(r.toPackPath(), r.toURL());
			}
		};

		sounds = new FCURegistrar<CustomSound>()
		{
			@Override
			public void onRegister(CustomSound r)
			{
				r.registerResources(FulcrumRegistry.this);
				r.addToJSON(soundsJSON);
			}
		};
	}

	public void complete()
	{
		allocator.registerAll(this);
		dependBlockModel("fulcrum_cauldron");
		dependBlockModel("fulcrum_pedestal");
		dependBlockModel("fulcrum_cube_all");
		dependBlockModel("fulcrum_cube_bottom_top");
		dependBlockModel("fulcrum_cube_cased");
		dependBlockModel("fulcrum_cube_column");
		dependBlockModel("fulcrum_cube_companion");
		dependBlockModel("fulcrum_cube_framed");
		dependBlockModel("fulcrum_cube_manual");
		dependBlockModel("fulcrum_cube_top");
		getPack().setResource("sounds.json", soundsJSON.toString(Fulcrum.minifyJSON ? 0 : 4));
		getPack().setResource("lang/en_us.lang", langBuild.toString());
	}

	private void dependBlockModel(String name)
	{
		model().register(new CustomModel(name, f().getResource("assets/models/block/" + name + ".json"), ModelType.BLOCK));
	}

	public FCURegistrar<CustomItem> item()
	{
		return items;
	}

	public FCURegistrar<CustomTexture> texture()
	{
		return textures;
	}

	public FCURegistrar<CustomSound> sound()
	{
		return sounds;
	}

	public FCURegistrar<CustomVorbis> vorbis()
	{
		return vorbis;
	}

	public FCURegistrar<CustomModel> model()
	{
		return models;
	}

	public FCURegistrar<CustomLang> lang()
	{
		return lang;
	}

	private ResourcePack getPack()
	{
		return f().getPack();
	}

	private FulcrumInstance f()
	{
		return FulcrumInstance.instance;
	}

	public FulcrumRegistry begin()
	{
		Mortar.callEvent(new FulcrumRegistryEvent());
		return this;
	}

	public AllocationBlock allocator()
	{
		return allocator;
	}
}
