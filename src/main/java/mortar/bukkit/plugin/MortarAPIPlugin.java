package mortar.bukkit.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.bukkit.Bukkit;

import mortar.api.sched.J;
import mortar.bukkit.command.Command;
import mortar.bukkit.command.Permission;
import mortar.compute.math.M;
import mortar.util.queue.PhantomQueue;
import mortar.util.queue.Queue;
import mortar.util.text.C;
import mortar.util.text.D;

public class MortarAPIPlugin extends MortarPlugin
{
	@Instance
	public static MortarAPIPlugin p;

	@Command
	private CommandMortar mort;

	@Command
	private CommandClearConsole cls;

	@Permission
	public static PermissionMortar perm;
	private static Queue<String> logQueue;

	@Override
	public void start()
	{
		M.initTicking();
		J.a(() -> checkForUpdates());
		J.sr(() -> flushLogBuffer(), 5);
		J.ar(() -> M.uptickAsync(), 0);
		J.sr(() -> M.uptick(), 0);
	}

	private void flushLogBuffer()
	{
		if(logQueue == null)
		{
			return;
		}

		while(logQueue.hasNext())
		{
			Bukkit.getConsoleSender().sendMessage(logQueue.next());
		}
	}

	@Override
	public void stop()
	{

	}

	@Override
	public String getTag(String t)
	{
		if(t.trim().isEmpty())
		{
			return Mortar.tag(getName());
		}

		return Mortar.tag(getName() + " " + C.GRAY + " - " + C.WHITE + t);
	}

	private void checkForUpdates()
	{
		try
		{
			D.as("Mortar Updater").l("Checking for Updates");
			URL dl = new URL("https://raw.githubusercontent.com/VolmitSoftware/Mortar/master/release/Mortar.jar");
			URL url = new URL("https://raw.githubusercontent.com/VolmitSoftware/Mortar/master/version.txt");
			InputStream in = url.openStream();
			BufferedReader bu = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
			String version = bu.readLine().trim().toLowerCase();
			String current = getDescription().getVersion().trim().toLowerCase();
			in.close();
			bu.close();

			if(version.equals(current))
			{
				D.as("Mortar Updater").l("Mortar " + current + " is up to date!");
			}

			else
			{
				D.as("Mortar Updater").l("Updates are avalible: " + current + " -> " + version);
				HttpURLConnection con = (HttpURLConnection) dl.openConnection();
				HttpURLConnection.setFollowRedirects(false);
				con.setConnectTimeout(10000);
				con.setReadTimeout(10000);
				D.as("Mortar Updater").l("Downloading Update v" + version);
				InputStream inx = con.getInputStream();
				File mortar = new File("plugins/update/Mortar-" + version + ".jar");
				FileOutputStream fos = new FileOutputStream(mortar);
				byte[] buf = new byte[16819];
				int r = 0;

				while((r = inx.read(buf)) != -1)
				{
					fos.write(buf, 0, r);
				}

				fos.close();
				inx.close();
				con.disconnect();
				D.as("Mortar Updater").l("Update v" + version + " downloaded.");
				D.as("Mortar Updater").l("Restart to apply this update.");
			}
		}

		catch(Throwable e)
		{
			D.as("Mortar Updater").f("Failed to check for updates.");
		}
	}

	public static void log(String string)
	{
		if(logQueue == null)
		{
			logQueue = new PhantomQueue<>();
		}

		logQueue.queue(string);
	}
}
