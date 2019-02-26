package mortar.bukkit.plugin;

import mortar.bukkit.command.Command;
import mortar.util.text.C;
import mortar.util.text.TXT;

public class MortarAPIPlugin extends MortarPlugin
{
	@Instance
	public static MortarAPIPlugin p;

	@Command
	private CommandMortar mort;

	@Command
	private CommandClearConsole cls;

	@Override
	public void start()
	{

	}

	@Override
	public void stop()
	{

	}

	@Override
	public String getTag(String subTag)
	{
		return TXT.makeTag(C.BLUE, C.DARK_GRAY, C.GRAY, "Mortar");
	}
}
