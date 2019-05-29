package mortar.api.pluginmessage;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import mortar.bukkit.plugin.Controller;
import mortar.bukkit.plugin.MortarAPIPlugin;

public class PluginMessageController extends Controller implements PluginMessageListener
{
	@Override
	public void start()
	{
		Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(MortarAPIPlugin.p, "BungeeCord");
		Bukkit.getServer().getMessenger().registerIncomingPluginChannel(MortarAPIPlugin.p, "BungeeCord", this);
	}

	@Override
	public void stop()
	{

	}

	@Override
	public void tick()
	{

	}

	public void send(Player p, String... strings)
	{
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		for(String i : strings)
		{
			out.writeUTF(i);
		}

		p.sendPluginMessage(MortarAPIPlugin.p, "BungeeCord", out.toByteArray());
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message)
	{
		if(!channel.equals("BungeeCord"))
		{
			return;
		}
	}
}
