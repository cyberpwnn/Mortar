package mortar.api.inventory;

import java.util.function.BiFunction;

import org.bukkit.entity.Player;

import mortar.bukkit.plugin.MortarAPIPlugin;

public class AnvilText
{
	public static void getText(Player p, String def, RString s)
	{
		try
		{
			new AnvilGUI(MortarAPIPlugin.p, p, def, new BiFunction<Player, String, String>()
			{
				@Override
				public String apply(Player t, String u)
				{
					s.onComplete(u);
					t.closeInventory();

					return "";
				}
			});
		}

		catch(Exception e)
		{

		}
	}
}