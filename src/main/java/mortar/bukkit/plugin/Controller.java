package mortar.bukkit.plugin;

import mortar.util.text.D;

public abstract class Controller implements IController
{
	private int tickRate;
	private String name;

	public Controller()
	{
		name = getClass().getSimpleName().replaceAll("Controller", "") + " Controller";
		tickRate = -1;
	}

	protected void setTickRate(int rate)
	{
		this.tickRate = rate;
	}

	protected void disableTicking()
	{
		setTickRate(-1);
	}

	@Override
	public void l(Object... l)
	{
		D.as(getName()).l(l);
	}

	@Override
	public void w(Object... l)
	{
		D.as(getName()).w(l);
	}

	@Override
	public void f(Object... l)
	{
		D.as(getName()).f(l);
	}

	@Override
	public void v(Object... l)
	{
		D.as(getName()).v(l);
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public abstract void start();

	@Override
	public abstract void stop();

	@Override
	public abstract void tick();

	@Override
	public int getTickInterval()
	{
		return tickRate;
	}
}
