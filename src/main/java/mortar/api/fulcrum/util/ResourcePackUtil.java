package mortar.api.fulcrum.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.URL;
import java.net.UnknownHostException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent.Status;

import mortar.api.fulcrum.FulcrumInstance;
import mortar.api.sched.J;
import mortar.api.sched.S;
import mortar.bukkit.plugin.MortarAPIPlugin;

public class ResourcePackUtil
{
	public void sendResourcePackPacket(Player p, String url)
	{
		p.setResourcePack(url);
	}

	public void sendResourcePackPacket(Player p, String url, byte[] hash)
	{
		p.setResourcePack(url, hash);
	}

	public void sendResourcePack(Player p, String url)
	{
		sendResourcePackPrepare(p, new Runnable()
		{
			@Override
			public void run()
			{
				sendResourcePackPacket(p, url);
			}
		});
	}

	public void sendResourcePackWeb(Player p, String pack)
	{
		J.a(() ->
		{
			String uurl = getServerPublicAddress();
			System.out.println(p.getName() + " -> " + p.getAddress().getAddress().getHostAddress());

			if(p.getAddress().getAddress().getHostAddress().equals("127.0.0.1"))
			{
				System.out.println("Client is on the same network as the server. Setting url to local");

				try
				{
					uurl = Inet4Address.getLocalHost().getHostAddress();
				}

				catch(UnknownHostException e)
				{
					e.printStackTrace();
				}
			}

			String url = uurl;

			try
			{
				new S()
				{
					@Override
					public void run()
					{
						sendResourcePackPrepare(p, new Runnable()
						{
							@Override
							public void run()
							{
								sendResourcePackPacket(p, "http://" + url + ":" + FulcrumInstance.instance.getWeb().getPort() + "/" + pack);
								System.out.println("Sending " + p + " DYNAMIC pack @ " + "http://" + url + ":" + FulcrumInstance.instance.getWeb().getPort() + "/" + pack);
							}
						});
					}
				};
			}

			catch(Exception e)
			{
				e.printStackTrace();
			}
		});
	}

	public void sendResourcePackPrepare(Player p, Runnable r)
	{
		J.s(() ->
		{
			r.run();
			MortarAPIPlugin.p.registerListener(new Listener()
			{
				@EventHandler
				public void on(PlayerResourcePackStatusEvent e)
				{
					if(e.getPlayer().equals(p))
					{
						if(e.getStatus().equals(Status.ACCEPTED))
						{

						}

						if(e.getStatus().equals(Status.FAILED_DOWNLOAD))
						{
							MortarAPIPlugin.p.unregisterListener(this);
						}

						if(e.getStatus().equals(Status.DECLINED))
						{
							p.kickPlayer("In multiplayer options re-allow resource packs.");
						}

						if(e.getStatus().equals(Status.SUCCESSFULLY_LOADED))
						{
							MortarAPIPlugin.p.unregisterListener(this);
						}
					}
				}

			});
		});
	}

	public String getServerPublicAddress()
	{
		try
		{
			BufferedReader pr = new BufferedReader(new InputStreamReader(new URL("http://checkip.amazonaws.com/").openStream()));
			String address = pr.readLine();

			pr.close();

			return address;
		}

		catch(Exception e)
		{
			return null;
		}
	}
}
