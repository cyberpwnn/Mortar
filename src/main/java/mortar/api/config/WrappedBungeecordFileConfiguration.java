package mortar.api.config;

import java.io.File;
import java.io.StringWriter;
import java.util.List;

import mortar.api.config.ConfigWrapper;
import mortar.lang.collection.GList;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class WrappedBungeecordFileConfiguration implements ConfigWrapper
{
	private ConfigurationProvider wrapped;
	private Configuration loadedWrapper;

	public WrappedBungeecordFileConfiguration()
	{
		wrapped = ConfigurationProvider.getProvider(YamlConfiguration.class);
		loadedWrapper = wrapped.load("");
	}

	@Override
	public void load(File f) throws Exception
	{
		wrapped.load(f);
	}

	@Override
	public void save(File f) throws Exception
	{
		wrapped.save(loadedWrapper, f);
	}

	@Override
	public String save()
	{
		StringWriter sw = new StringWriter();
		wrapped.save(loadedWrapper, sw);
		return sw.toString();
	}

	@Override
	public void load(String s) throws Exception
	{
		loadedWrapper = wrapped.load(s);
	}

	@Override
	public void set(String key, Object oo)
	{
		Object o = null;

		if(oo instanceof List)
		{
			o = GList.asStringList((List<?>) oo);
		}

		else
		{
			o = oo;
		}

		loadedWrapper.set(key, o);
	}

	@Override
	public Object get(String key)
	{
		Object o = loadedWrapper.get(key);

		if(o instanceof List)
		{
			return GList.asStringList((List<?>) o);
		}

		return o;
	}

	@Override
	public GList<String> keys()
	{
		return new GList<String>(loadedWrapper.getKeys());
	}

	@Override
	public boolean contains(String key)
	{
		return loadedWrapper.contains(key);
	}
}
