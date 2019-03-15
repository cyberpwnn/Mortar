package mortar.scm;

import java.io.File;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import mortar.api.sound.Audio;
import mortar.api.sound.GSound;
import mortar.api.world.Cuboid;
import mortar.api.world.P;
import mortar.bukkit.command.MortarCommand;
import mortar.bukkit.command.MortarSender;
import mortar.bukkit.plugin.Mortar;
import mortar.bukkit.plugin.MortarAPIPlugin;
import mortar.lang.collection.GList;
import mortar.logic.format.F;
import mortar.util.text.C;

public class CommandSCM extends MortarCommand
{
	public CommandSCM()
	{
		super("scm");
		requiresPermission(MortarAPIPlugin.perm.scm);
	}

	@Override
	public boolean handle(MortarSender sender, String[] args)
	{
		if(args.length == 0)
		{
			sender.sendMessage("/scm wand");
			sender.sendMessage("/scm update");
			sender.sendMessage("/scm list");
			sender.sendMessage("/scm status");
			sender.sendMessage("/scm save <id>");
			sender.sendMessage("/scm place <id>");
			sender.sendMessage("/scm delete <id>");
			return true;
		}

		else if(args[0].equalsIgnoreCase("wand"))
		{
			GSound g = new GSound(Sound.BLOCK_END_PORTAL_FRAME_FILL);
			g.setPitch(0.5f);
			g.play(sender.player());
			ItemStack is = new ItemStack(Material.IRON_AXE);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(C.YELLOW + "SCM Wand");
			Location ll = (sender.player()).getLocation();
			GList<String> s = new GList<String>();
			s.add(C.AQUA + "A: " + ll.getWorld().getName() + "@" + ll.getBlockX() + "." + ll.getBlockY() + "." + ll.getBlockZ());
			s.add(C.AQUA + "B: " + ll.getWorld().getName() + "@" + ll.getBlockX() + "." + ll.getBlockY() + "." + ll.getBlockZ());
			im.setLore(s);
			is.setItemMeta(im);
			(sender.player()).getInventory().addItem(is);
		}

		else if(args[0].equalsIgnoreCase("list"))
		{
			sender.sendMessage("Listing " + Mortar.getController(SCMController.class).getVolumes().size() + " SCM Volumes");

			for(String i : Mortar.getController(SCMController.class).getVolumes().k())
			{
				sender.sendMessage(i);
			}
		}

		else if(args[0].equalsIgnoreCase("status"))
		{
			int s = 0;

			for(String i : Mortar.getController(SCMController.class).getVolumes().k())
			{
				s += Mortar.getController(SCMController.class).getVolumes().get(i).getVectorSchematic().getTypes().size();
			}

			sender.sendMessage("There are " + Mortar.getController(SCMController.class).getVolumes().size() + " SCM Volumes");
			sender.sendMessage("Cached " + F.f(s) + " SCM Vector Maps");
		}

		else if(args[0].equalsIgnoreCase("update"))
		{
			Mortar.getController(SCMController.class).getVolumes().clear();
			File gf = Mortar.getController(SCMController.class).getSCMFolder();

			if(gf.exists())
			{
				for(File i : gf.listFiles())
				{
					sender.sendMessage("Loading " + i.getName());

					try
					{
						IVolume v = new SCMVolume(i);
						Mortar.getController(SCMController.class).getVolumes().put(i.getName().replace(".scmv", ""), v);
					}

					catch(IOException e)
					{
						e.printStackTrace();
					}
				}
			}

			sender.sendMessage("Updated " + Mortar.getController(SCMController.class).getVolumes().size() + " SCM Volumes");
		}

		else if(args.length >= 1)
		{
			if(args[0].equalsIgnoreCase("delete"))
			{
				if(args.length == 2)
				{
					File gf = Mortar.getController(SCMController.class).getSCMFile(args[1]);

					if(gf.exists())
					{
						gf.delete();
						Mortar.getController(SCMController.class).getVolumes().remove(args[1]);
						new Audio().s(Sound.BLOCK_FIRE_EXTINGUISH).vp(1f, 1.5f).play(sender.player());
						sender.sendMessage(args[1] + " Deleted.");
					}

					else
					{
						sender.sendMessage(args[1] + " does not exist.");
					}
				}

				else
				{
					sender.sendMessage("/scm delete <id>");
				}
			}

			else if(args[0].equalsIgnoreCase("save"))
			{
				if(args.length == 2)
				{
					Location[] f = Mortar.getController(SCMController.class).getSelection((sender.player()));

					if(f != null)
					{
						Cuboid c = new Cuboid(f[0], f[1]);

						if(c.volume() > 8192)
						{
							sender.sendMessage("SCM Volume too large.");
							return true;
						}

						try
						{
							IVolume vv = new SCMVolume(c, PermutationType.ANY_AXIS);
							vv.save(Mortar.getController(SCMController.class).getSCMFile(args[1]));
							new Audio().s(Sound.BLOCK_ENCHANTMENT_TABLE_USE).vp(1f, 1.5f).play(sender.player());
							Mortar.getController(SCMController.class).getVolumes().put(args[1], vv);
						}

						catch(IOException e)
						{
							e.printStackTrace();
						}

						sender.sendMessage("SCM Volume saved as " + args[1]);
					}

					else
					{
						sender.sendMessage("Hold a wand with a selection first (/scm wand)");
					}

				}

				else
				{
					sender.sendMessage("/scm save <id>");
				}
			}

			else if(args[0].equalsIgnoreCase("place"))
			{
				if(args.length == 2)
				{
					if(Mortar.getController(SCMController.class).getVolumes().containsKey(args[1]))
					{
						Location lx = P.targetBlock((sender.player()), 12);
						new Audio().s(Sound.BLOCK_ENCHANTMENT_TABLE_USE).vp(1f, 1.5f).play(sender.player());
						Mortar.getController(SCMController.class).getVolumes().get(args[1]).place(lx);
						sender.sendMessage("SCM Volume " + args[1] + " placed at target.");
					}

					else
					{
						sender.sendMessage("SCM Volume " + args[1] + " not found. (try /scm update");
					}
				}

				else
				{
					sender.sendMessage("/scm place <id>");
				}
			}
		}

		return true;
	}

}
