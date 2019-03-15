package mortar.api.fx;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface Effect
{
	public void play(Location l);

	public void play(Player p, Location l);
}
