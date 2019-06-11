package mortar.api.tome;

import java.io.IOException;

import org.bukkit.inventory.ItemStack;

import mortar.api.sched.J;
import mortar.bukkit.command.MortarCommand;
import mortar.bukkit.command.MortarSender;
import mortar.bukkit.plugin.MortarAPIPlugin;
import mortar.lang.collection.GList;
import mortar.lang.collection.GMap;
import mortar.lang.collection.LGMap;
import mortar.util.text.Alphabet;

public class CommandCatalogue extends MortarCommand
{
	public CommandCatalogue()
	{
		super("catalogue", "list", "l", "c");
		requiresPermission(MortarAPIPlugin.perm);
	}

	@Override
	public boolean handle(MortarSender sender, String[] args)
	{
		J.a(() ->
		{
			GMap<String, Tome> rtomes = TomeLibrary.getInstance().getTomes();
			GList<Tome> tomes = rtomes.sortV();
			LGMap<Alphabet, TomeSection> sections = new LGMap<>();

			//@builder
			Tome tome = new Tome();
			tome.setName("Tome Catalogue");
			tome.setAuthor("Mortar");
			tome.getRoot()
			.add(new TomeMeta("tableOfContents", "true"))
			.add(new TomeMeta("frontPage", "true"));
			//@done

			for(Tome i : tomes)
			{
				if(!sections.containsKey(i.getLetter()))
				{
					sections.put(i.getLetter(), new TomeSection((i.getLetter().getChar() + "").toUpperCase()));
				}

				TomeSection s = sections.get(i.getLetter());
				//@builder
				s.add(new TomeParagraph()
						.add(new TomeFormat()
								.setFormat("italic")
								.setOnClick("run /tome give " + i.getId())
								.add(new TomeHover()
										.add(new TomeParagraph().add(i.getName()))
										.add(new TomeParagraph().add("by " + i.getAuthor()))
										.add(new TomeParagraph().add(" "))
										.add(new TomeParagraph().add("Click to add this tome to your inventory.")))
								.add("\u270E " + i.getName())));
				//@done
			}

			for(Alphabet i : sections.k())
			{
				tome.getRoot().add(sections.get(i));
			}
			ItemStack listing = tome.toItemStack();

			try
			{
				System.out.println(tome.save());
			}

			catch(IOException e)
			{
				e.printStackTrace();
			}


			J.s(() -> sender.player().getInventory().addItem(listing));
		});

		return true;
	}

}
