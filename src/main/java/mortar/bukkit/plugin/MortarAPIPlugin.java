package mortar.bukkit.plugin;

import org.bukkit.Bukkit;

import mortar.api.sched.J;
import mortar.bukkit.command.Command;
import mortar.bukkit.command.Permission;
import mortar.compute.math.M;
import mortar.util.queue.PhantomQueue;
import mortar.util.queue.Queue;
import mortar.util.text.C;

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

		}

		catch(Throwable e)
		{

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
