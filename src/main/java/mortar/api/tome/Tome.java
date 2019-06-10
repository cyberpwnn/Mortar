package mortar.api.tome;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaBook;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftChatMessage;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.BookMeta.Generation;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.SAXReader;

import mortar.lang.collection.GList;
import mortar.lang.collection.GMap;
import mortar.lang.json.JSONObject;
import mortar.logic.format.F;
import mortar.logic.io.VIO;
import net.minecraft.server.v1_12_R1.ChatClickable;
import net.minecraft.server.v1_12_R1.ChatClickable.EnumClickAction;
import net.minecraft.server.v1_12_R1.ChatHoverable;
import net.minecraft.server.v1_12_R1.ChatHoverable.EnumHoverAction;
import net.minecraft.server.v1_12_R1.ChatModifier;
import net.minecraft.server.v1_12_R1.EnumChatFormat;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;

public class Tome
{
	private TomeComponentBook root;
	private boolean preprocessed;

	public Tome()
	{
		root = new TomeComponentBook();
		preprocessed = false;
	}

	private void preprocessTome()
	{
		if(preprocessed)
		{
			return;
		}

		preprocessed = true;

		int m = 0;

		for(TomeComponent a : root.getComponents())
		{
			if(a instanceof TomeSection)
			{
				TomeSection section = (TomeSection) a;
				section.getComponents().add(0, new TomeAnchor("s" + m));
				section.getComponents().add(0, new TomeHeader().add(section.getSectionName()));
				m++;
			}
		}
	}

	public BookMeta export(BookMeta meta)
	{
		CraftMetaBook book = (CraftMetaBook) meta;
		GMap<String, Integer> anchorPages = new GMap<>();
		GList<String> tables = new GList<>();
		int currentPage = 0;
		int currentLine = 0;
		int maxLines = 11;
		int maxCharacters = 25;
		meta.setDisplayName(getRoot().getName());
		meta.setAuthor(getRoot().getAuthor());
		meta.setGeneration(Generation.TATTERED);
		book.pages.clear();
		IChatBaseComponent content = CraftChatMessage.fromString("", true)[0];

		for(TomeComponent a : root.getComponents())
		{
			if(a instanceof TomeSection)
			{
				TomeSection section = (TomeSection) a;
				tables.add(section.getSectionName());

				for(TomeComponent b : section.getComponents())
				{
					if(b instanceof TomeHeader)
					{
						IChatBaseComponent line = exportChildren((TomeParagraph) b);
						content.addSibling(line);
					}

					else if(b instanceof TomeParagraph)
					{
						IChatBaseComponent line = exportChildren((TomeParagraph) b);
						content.addSibling(line);
					}

					else if(b instanceof TomeAnchor)
					{
						IChatBaseComponent line = CraftChatMessage.fromString("!!anchor!!:" + ((TomeAnchor) b).getAnchorName(), true)[0];
						content.addSibling(line);
					}

					else if(b instanceof TomeUnorederdList)
					{
						for(TomeComponent c : b.getComponents())
						{
							if(c instanceof TomeParagraph)
							{
								IChatBaseComponent line = exportChildren((TomeParagraph) c, "\u2712");
								content.addSibling(line);
							}

							else if(c instanceof TomeAnchor)
							{

							}
						}

						content.a("\n");
					}

					else if(b instanceof TomeOrderedList)
					{
						int m = 1;

						for(TomeComponent c : b.getComponents())
						{
							if(c instanceof TomeParagraph)
							{
								IChatBaseComponent line = exportChildren((TomeParagraph) c, m + ". ");
								content.addSibling(line);
								m++;
							}

							else if(c instanceof TomeAnchor)
							{

							}
						}

						content.a("\n");
					}
				}
			}
		}

		GList<IChatBaseComponent> elements = new GList<>(content.a());
		GMap<Integer, GList<IChatBaseComponent>> pageListing = new GMap<>();

		while(!elements.isEmpty())
		{
			IChatBaseComponent next = elements.get(0);
			String raw = next.toPlainText();
			int nls = StringUtils.countMatches(raw, '\n');
			int lineConsumption = 0;

			if(!raw.contains("!!anchor!!"))
			{
				int length = raw.length();
				lineConsumption = (int) Math.ceil((double) length / (double) maxCharacters);
				lineConsumption += (nls - 1) >= 0 ? (nls - 1) : 0;

				if(lineConsumption + currentLine > maxLines)
				{
					currentPage++;
					currentLine = 0;
				}
			}

			if(!pageListing.containsKey(currentPage))
			{
				pageListing.put(currentPage, new GList<>());
			}

			pageListing.get(currentPage).add(next);
			elements.remove(0);
			currentLine += lineConsumption;
		}

		GList<IChatBaseComponent> prePages = new GList<>();
		prePages.add(exportFrontPage());
		prePages.addAll(exportTableOfContents(tables));

		for(Integer i : pageListing.k())
		{
			IChatBaseComponent cp = CraftChatMessage.fromString("", true)[0];
			cp.a().addAll(pageListing.get(i));
			prePages.add(cp);
		}

		int pg = 1;

		for(IChatBaseComponent i : prePages)
		{
			for(IChatBaseComponent j : new GList<>(i.a()))
			{
				for(IChatBaseComponent k : new GList<>(j.a()))
				{
					for(IChatBaseComponent l : new GList<>(k.a()))
					{
						for(IChatBaseComponent m : new GList<>(l.a()))
						{
							for(IChatBaseComponent n : new GList<>(m.a()))
							{
								if(n.getText().startsWith("!!anchor!!:"))
								{
									anchorPages.put(n.getText().split(":")[1], pg);
									m.a().remove(n);
								}
							}

							if(m.getText().startsWith("!!anchor!!:"))
							{
								anchorPages.put(m.getText().split(":")[1], pg);
								l.a().remove(m);
							}
						}

						if(l.getText().startsWith("!!anchor!!:"))
						{
							anchorPages.put(l.getText().split(":")[1], pg);
							k.a().remove(l);
						}
					}

					if(k.getText().startsWith("!!anchor!!:"))
					{
						anchorPages.put(k.getText().split(":")[1], pg);
						j.a().remove(k);
					}
				}

				if(j.getText().startsWith("!!anchor!!:"))
				{
					anchorPages.put(j.getText().split(":")[1], pg);
					i.a().remove(j);
				}
			}

			pg++;
		}

		for(IChatBaseComponent i : prePages)
		{
			try
			{
				if(i.getChatModifier().h().a().equals(EnumClickAction.CHANGE_PAGE))
				{
					Integer nt = anchorPages.get(i.getChatModifier().h().b());

					if(nt != null)
					{
						i.getChatModifier().setChatClickable(new ChatClickable(EnumClickAction.CHANGE_PAGE, "" + nt));
					}

					else
					{
						i.getChatModifier().setChatClickable(null);
					}
				}
			}

			catch(Throwable e)
			{

			}

			for(IChatBaseComponent j : i.a())
			{
				try
				{
					if(j.getChatModifier().h().a().equals(EnumClickAction.CHANGE_PAGE))
					{
						Integer nt = anchorPages.get(j.getChatModifier().h().b());

						if(nt != null)
						{
							j.getChatModifier().setChatClickable(new ChatClickable(EnumClickAction.CHANGE_PAGE, "" + nt));
						}

						else
						{
							j.getChatModifier().setChatClickable(null);
						}
					}
				}

				catch(Throwable e)
				{

				}

				for(IChatBaseComponent k : j.a())
				{
					try
					{
						if(k.getChatModifier().h().a().equals(EnumClickAction.CHANGE_PAGE))
						{
							Integer nt = anchorPages.get(k.getChatModifier().h().b());

							if(nt != null)
							{
								k.getChatModifier().setChatClickable(new ChatClickable(EnumClickAction.CHANGE_PAGE, "" + nt));
							}

							else
							{
								k.getChatModifier().setChatClickable(null);
							}
						}
					}

					catch(Throwable e)
					{

					}

					for(IChatBaseComponent l : k.a())
					{
						try
						{
							if(l.getChatModifier().h().a().equals(EnumClickAction.CHANGE_PAGE))
							{
								Integer nt = anchorPages.get(l.getChatModifier().h().b());

								if(nt != null)
								{
									l.getChatModifier().setChatClickable(new ChatClickable(EnumClickAction.CHANGE_PAGE, "" + nt));
								}

								else
								{
									l.getChatModifier().setChatClickable(null);
								}
							}
						}

						catch(Throwable e)
						{

						}

						for(IChatBaseComponent m : l.a())
						{
							try
							{
								if(m.getChatModifier().h().a().equals(EnumClickAction.CHANGE_PAGE))
								{
									Integer nt = anchorPages.get(m.getChatModifier().h().b());

									if(nt != null)
									{
										m.getChatModifier().setChatClickable(new ChatClickable(EnumClickAction.CHANGE_PAGE, "" + nt));
									}

									else
									{
										m.getChatModifier().setChatClickable(null);
									}
								}
							}

							catch(Throwable e)
							{

							}

							for(IChatBaseComponent n : m.a())
							{
								try
								{
									if(n.getChatModifier().h().a().equals(EnumClickAction.CHANGE_PAGE))
									{
										Integer nt = anchorPages.get(n.getChatModifier().h().b());

										if(nt != null)
										{
											n.getChatModifier().setChatClickable(new ChatClickable(EnumClickAction.CHANGE_PAGE, "" + nt));
										}

										else
										{
											n.getChatModifier().setChatClickable(null);
										}
									}
								}

								catch(Throwable e)
								{

								}
							}
						}
					}
				}
			}
		}

		for(IChatBaseComponent i : prePages)
		{
			System.out.println(new JSONObject(IChatBaseComponent.ChatSerializer.a(i)).toString(2));
		}

		book.pages.addAll(prePages);

		return book;
	}

	private GList<IChatBaseComponent> exportTableOfContents(GList<String> tables)
	{
		GList<IChatBaseComponent> pg = new GList<>();

		int sec = 0;
		int toc = 0;
		IChatBaseComponent pageBuffer = CraftChatMessage.fromString("", true)[0];
		ChatModifier cmodx = new ChatModifier();
		cmodx.setBold(true);
		cmodx.setUnderline(true);
		IChatBaseComponent t = CraftChatMessage.fromString("Table Of Contents", true)[0];
		t.setChatModifier(cmodx);
		pageBuffer.a().add(t);
		pageBuffer.a("\n\n");
		boolean ad = false;
		for(String i : tables)
		{
			ad = true;
			ChatModifier cmod = new ChatModifier();
			cmod.setItalic(true);
			cmod.setChatClickable(new ChatClickable(EnumClickAction.CHANGE_PAGE, "s" + sec));
			cmod.setChatHoverable(new ChatHoverable(EnumHoverAction.SHOW_TEXT, CraftChatMessage.fromString("Click to see '" + i + "'.", true)[0]));
			IChatBaseComponent content = CraftChatMessage.fromString("", true)[0];
			IChatBaseComponent nib = CraftChatMessage.fromString("\u270E ", true)[0];
			IChatBaseComponent text = CraftChatMessage.fromString(i, true)[0];
			text.setChatModifier(cmod);
			content.a().add(nib);
			content.a().add(text);
			pageBuffer.a().add(content);
			toc++;
			sec++;

			if(toc > 11)
			{
				ad = false;
				toc = 0;
				pg.add(pageBuffer);
				pageBuffer = CraftChatMessage.fromString("", true)[0];
				cmodx = new ChatModifier();
				cmodx.setBold(true);
				cmodx.setUnderline(true);
				t = CraftChatMessage.fromString("Table Of Contents", true)[0];
				t.setChatModifier(cmodx);
				pageBuffer.a().add(t);
				pageBuffer.a("\n\n");
			}

			else
			{
				pageBuffer.a("\n");
			}
		}

		if(ad)
		{
			pg.add(pageBuffer);
		}

		return pg;
	}

	private IChatBaseComponent exportFrontPage()
	{
		IChatBaseComponent cFrontPage = CraftChatMessage.fromString("", true)[0];
		IChatBaseComponent cInd = CraftChatMessage.fromString(F.repeat("\n", 5), true)[0];
		IChatBaseComponent cTitle = CraftChatMessage.fromString(getName() + "\n", true)[0];
		IChatBaseComponent cBy = CraftChatMessage.fromString("By " + getAuthor() + "\n", true)[0];
		IChatBaseComponent cVolume = CraftChatMessage.fromString("Vol. " + getVolume() + "\n", true)[0];
		ChatModifier boldUnderline = new ChatModifier();
		boldUnderline.setBold(true);
		boldUnderline.setUnderline(true);
		ChatModifier italic = new ChatModifier();
		italic.setItalic(true);
		cTitle.setChatModifier(boldUnderline);
		cBy.setChatModifier(italic);
		cVolume.setChatModifier(italic);
		cFrontPage.a().add(cInd);
		cFrontPage.a().add(cTitle);
		cFrontPage.a().add(cBy);

		if(!getVolume().isEmpty())
		{
			cFrontPage.a().add(cVolume);
		}

		return cFrontPage;
	}

	private IChatBaseComponent exportChildren(TomeParagraph paragraph)
	{
		return exportChildren(paragraph, "");
	}

	private IChatBaseComponent exportChildren(TomeHover paragraph)
	{
		IChatBaseComponent line = CraftChatMessage.fromString("", true)[0];

		for(TomeComponent i : paragraph.getComponents())
		{
			if(i instanceof TomeUnorederdList)
			{
				for(TomeComponent c : i.getComponents())
				{
					if(c instanceof TomeParagraph)
					{
						IChatBaseComponent lx = exportChildren((TomeParagraph) c, "\u2712");
						line.addSibling(lx);
					}

					else if(c instanceof TomeAnchor)
					{

					}
				}
			}

			if(i instanceof TomeOrderedList)
			{
				int m = 1;

				for(TomeComponent c : i.getComponents())
				{
					if(c instanceof TomeParagraph)
					{
						IChatBaseComponent lx = exportChildren((TomeParagraph) c, m + ". ");
						line.addSibling(lx);
						m++;
					}

					else if(c instanceof TomeAnchor)
					{

					}
				}
			}

			if(i instanceof TomeParagraph)
			{
				line.a(((TomeParagraph) i).getText());
			}

			if(i instanceof TomeFormat)
			{
				line.addSibling(exportChildren((TomeFormat) i));
			}

			if(i instanceof TomeText)
			{
				line.a(((TomeText) i).getText());
			}
		}

		return line;
	}

	private IChatBaseComponent exportChildren(TomeParagraph paragraph, String prefixLine)
	{
		IChatBaseComponent line = CraftChatMessage.fromString("", true)[0];
		ChatModifier cmod = new ChatModifier();

		if(paragraph instanceof TomeHeader)
		{
			cmod.setBold(true);
			cmod.setUnderline(true);
			line.setChatModifier(cmod);
		}

		if(!prefixLine.isEmpty())
		{
			line.a(prefixLine);
		}

		for(TomeComponent i : paragraph.getComponents())
		{
			if(i instanceof TomeText)
			{
				line.a(((TomeText) i).getText());
			}

			if(i instanceof TomeFormat)
			{
				line.addSibling(exportChildren((TomeFormat) i));
			}
		}

		line.a("\n");

		if(paragraph instanceof TomeHeader)
		{
			line.a("\n");
		}

		return line;
	}

	private String splitFirst(String s, String sp)
	{
		String v = "";
		boolean f = true;

		for(String i : s.split("\\Q" + sp + "\\E"))
		{
			if(f)
			{
				f = false;
				continue;
			}

			v += sp + i;
		}

		return v.substring(sp.length());
	}

	private IChatBaseComponent exportChildren(TomeFormat format)
	{
		ChatModifier cmod = new ChatModifier();

		if(format.getOnClick() != null)
		{
			String command = format.getOnClick();

			if(command.startsWith("run "))
			{
				cmod.setChatClickable(new ChatClickable(EnumClickAction.RUN_COMMAND, splitFirst(command, " ")));
			}

			if(command.startsWith("suggest "))
			{
				cmod.setChatClickable(new ChatClickable(EnumClickAction.SUGGEST_COMMAND, splitFirst(command, " ")));
			}

			if(command.startsWith("url "))
			{
				cmod.setChatClickable(new ChatClickable(EnumClickAction.OPEN_URL, splitFirst(command, " ")));
			}

			if(command.startsWith("open "))
			{
				cmod.setChatClickable(new ChatClickable(EnumClickAction.OPEN_FILE, splitFirst(command, " ")));
			}

			if(command.startsWith("goto "))
			{
				cmod.setChatClickable(new ChatClickable(EnumClickAction.CHANGE_PAGE, splitFirst(command, " ")));
			}
		}

		if(format.getColor() != null)
		{
			cmod.setColor(EnumChatFormat.valueOf(format.getColor().toUpperCase()));
		}

		if(format.getFormat() != null)
		{
			if(format.getFormat().contains("bold"))
			{
				cmod.setBold(true);
			}

			if(format.getFormat().contains("underline"))
			{
				cmod.setUnderline(true);
			}

			if(format.getFormat().contains("strikethrough"))
			{
				cmod.setStrikethrough(true);
			}

			if(format.getFormat().contains("italic"))
			{
				cmod.setItalic(true);
			}

			if(format.getFormat().contains("magic"))
			{
				cmod.setRandom(true);
			}
		}

		String sym = "";

		for(TomeComponent i : format.getComponents())
		{
			if(i instanceof TomeHover)
			{
				cmod.setChatHoverable(new ChatHoverable(EnumHoverAction.SHOW_TEXT, exportChildren((TomeHover) i)));
			}

			else if(i instanceof TomeText)
			{
				sym += ((TomeText) i).getText();
			}
		}

		IChatBaseComponent sub = CraftChatMessage.fromString(sym, true)[0];
		sub.setChatModifier(cmod);

		return sub;
	}

	public String getName()
	{
		return getRoot().getName();
	}

	public String getId()
	{
		return getRoot().getId();
	}

	public String getVolume()
	{
		return getRoot().getVolume();
	}

	public void setName(String name)
	{
		getRoot().setName(name);
	}

	public String getAuthor()
	{
		return getRoot().getAuthor();
	}

	public void setAuthor(String author)
	{
		getRoot().setAuthor(author);
	}

	public TomeComponentBook getRoot()
	{
		return root;
	}

	public void setRoot(TomeComponentBook root)
	{
		this.root = root;
	}

	public void load(String jarPath, Class<?> anchor) throws DocumentException, IOException
	{
		VIO.readAll(anchor.getResourceAsStream("/" + (jarPath.startsWith("/") ? jarPath.substring(1) : jarPath)));
	}

	public void load(File f) throws DocumentException, IOException
	{
		load(VIO.readAll(f));
	}

	public void load(String xml) throws DocumentException
	{
		SAXReader reader = new SAXReader();
		Document document = reader.read(new StringReader(xml));
		setRoot(new TomeComponentBook());
		getRoot().read(document);
		preprocessTome();
	}

	public void save(File f) throws IOException
	{
		VIO.writeAll(f, save());
	}

	public String save() throws IOException
	{
		Document document = DocumentHelper.createDocument();
		getRoot().construct(document);

		StringWriter w = new StringWriter();
		document.write(w);

		return format(w.toString());
	}

	private final static String format(String input)
	{
		return format(input, 4);
	}

	private final static String format(String input, int indent)
	{
		try
		{
			Source xmlInput = new StreamSource(new StringReader(input));
			StringWriter stringWriter = new StringWriter();
			StreamResult xmlOutput = new StreamResult(stringWriter);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory.setAttribute("indent-number", indent);
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(xmlInput, xmlOutput);
			return xmlOutput.getWriter().toString();
		}

		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}
