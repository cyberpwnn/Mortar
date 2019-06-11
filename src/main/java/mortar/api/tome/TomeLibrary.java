package mortar.api.tome;

import java.io.File;
import java.io.IOException;

import mortar.lang.collection.GMap;
import mortar.util.text.D;

public class TomeLibrary
{
	public static TomeLibrary library;
	private GMap<String, Tome> tomes;

	public static TomeLibrary getInstance()
	{
		if(library == null)
		{
			return new TomeLibrary();
		}

		return library;
	}

	private TomeLibrary()
	{
		library = this;
		tomes = new GMap<>();
		reload();
	}

	public GMap<String, Tome> getTomes()
	{
		return tomes.copy();
	}

	public void reload()
	{
		tomes.clear();

		File folder = new File("tomes");

		if(folder.exists())
		{
			for(File i : folder.listFiles())
			{
				if(i.getName().endsWith(".xml"))
				{
					try
					{
						Tome tome = new Tome();
						tome.load(i);
						register(tome);
						D.as("TomeLibrary").l("Loaded Tome " + i.getName() + " [" + tome.getId() + "] (" + tome.getName() + " by " + tome.getAuthor() + ")");
					}

					catch(Throwable e)
					{
						D.as("TomeLibrary").w("Cannot load Tome: " + i.getName());
						D.as("TomeLibrary").w(e.getMessage());
					}
				}
			}

			D.as("TomeLibrary").l("Loaded " + tomes.size() + " tomes.");
		}
	}

	public void saveTome(Tome tome)
	{
		File f = new File(new File("tomes"), tome.getId());
		f.getParentFile().mkdirs();

		try
		{
			tome.save(f);
			D.as("TomeLibrary").l("Saved Tome " + f.getName() + " [" + tome.getId() + "] (" + tome.getName() + " by " + tome.getAuthor() + ")");
		}

		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public void register(Tome tome)
	{
		tomes.put(tome.getId(), tome);
	}
}
