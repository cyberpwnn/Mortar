package mortar.api.fulcrum;

import java.io.IOException;

import mortar.api.fulcrum.object.FCUItem;
import mortar.api.fulcrum.object.FCULang;
import mortar.api.fulcrum.object.FCUModel;
import mortar.api.fulcrum.object.FCUSound;
import mortar.api.fulcrum.object.FCUTexture;
import mortar.api.fulcrum.object.FCUVorbis;
import mortar.api.fulcrum.registry.FCURegistrar;
import mortar.api.fulcrum.util.AllocationBlock;
import mortar.api.resourcepack.ModelType;
import mortar.api.resourcepack.ResourcePack;
import mortar.bukkit.plugin.Mortar;
import mortar.lang.json.JSONException;
import mortar.lang.json.JSONObject;
import mortar.logic.io.VIO;

public class FulcrumRegistry
{
	private final FCURegistrar<FCUItem> items;
	private final FCURegistrar<FCUTexture> textures;
	private final FCURegistrar<FCUModel> models;
	private final FCURegistrar<FCUSound> sounds;
	private final FCURegistrar<FCUVorbis> vorbis;
	private final FCURegistrar<FCULang> lang;
	private final StringBuilder langBuild;
	private final AllocationBlock allocator;
	private final JSONObject soundsJSON;

	public FulcrumRegistry() throws JSONException, IOException
	{
		allocator = new AllocationBlock(Fulcrum.allocationStrategy);
		allocator.addDefaultUnits();
		langBuild = new StringBuilder();
		soundsJSON = new JSONObject(VIO.readAll(getClass().getResourceAsStream("/assets/sounds-default.json")));

		lang = new FCURegistrar<FCULang>()
		{
			@Override
			public void onRegister(FCULang r)
			{
				langBuild.append(r.toLine() + "\n");
			}
		};

		items = new FCURegistrar<FCUItem>()
		{
			@Override
			public void onRegister(FCUItem r)
			{
				allocator.allocate(r);
				r.registerResources(FulcrumRegistry.this);
			}
		};

		textures = new FCURegistrar<FCUTexture>()
		{
			@Override
			public void onRegister(FCUTexture r)
			{
				getPack().setResource(r.toPackPath(), r.toURL());
			}
		};

		vorbis = new FCURegistrar<FCUVorbis>()
		{
			@Override
			public void onRegister(FCUVorbis r)
			{
				getPack().setResource(r.toPackPath(), r.toURL());
			}
		};

		models = new FCURegistrar<FCUModel>()
		{
			@Override
			public void onRegister(FCUModel r)
			{
				getPack().setResource(r.toPackPath(), r.toURL());
			}
		};

		sounds = new FCURegistrar<FCUSound>()
		{
			@Override
			public void onRegister(FCUSound r)
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
		model().register(new FCUModel(name, getClass(), "assets/models/block/" + name + ".json", ModelType.BLOCK));
	}

	public FCURegistrar<FCUItem> item()
	{
		return items;
	}

	public FCURegistrar<FCUTexture> texture()
	{
		return textures;
	}

	public FCURegistrar<FCUSound> sound()
	{
		return sounds;
	}

	public FCURegistrar<FCUVorbis> vorbis()
	{
		return vorbis;
	}

	public FCURegistrar<FCUModel> model()
	{
		return models;
	}

	public FCURegistrar<FCULang> lang()
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
