package mortar.api.rift;

import org.spigotmc.TickLimiter;

import mortar.compute.math.Average;
import mortar.compute.math.M;

public class SpecializedTickLimiter extends TickLimiter // heh
{
	public double rMaxTime;
	public long rStartTime;
	public double rLastTime;
	public long rMark;
	public double tMaxTime;
	public Average atimes = new Average(75);
	public Average adropped = new Average(5);
	private int droppedTicks;

	public SpecializedTickLimiter(double maxtime)
	{
		super((int) maxtime);

		this.rMark = M.ns();
		this.rMaxTime = maxtime;
		this.tMaxTime = maxtime;
		this.droppedTicks = 0;
	}

	@Override
	public void initTick()
	{
		rLastTime = (double) (rMark - rStartTime) / 1000000.0;
		this.rStartTime = M.ns();
		atimes.put(M.clip(rLastTime, 0, 1000));
		adropped.put(droppedTicks);
		droppedTicks = 0;
	}

	@Override
	public boolean shouldContinue()
	{
		long remaining = M.ns() - this.rStartTime;
		boolean con = remaining < (long) (this.rMaxTime * 1000000.0);

		if(con)
		{
			rMark = M.ns();
		}

		else
		{
			droppedTicks++;
		}

		return con;
	}

	public double getrMaxTime()
	{
		return rMaxTime;
	}

	public void setrMaxTime(double rMaxTime)
	{
		this.rMaxTime = rMaxTime;
	}

	public long getrStartTime()
	{
		return rStartTime;
	}

	public void setrStartTime(long rStartTime)
	{
		this.rStartTime = rStartTime;
	}

	public double getrLastTime()
	{
		return rLastTime;
	}

	public void setrLastTime(double rLastTime)
	{
		this.rLastTime = rLastTime;
	}

	public long getrMark()
	{
		return rMark;
	}

	public void setrMark(long rMark)
	{
		this.rMark = rMark;
	}

	public double gettMaxTime()
	{
		return tMaxTime;
	}

	public void settMaxTime(double tMaxTime)
	{
		this.tMaxTime = tMaxTime;
	}

	public Average getAtimes()
	{
		return atimes;
	}

	public void setAtimes(Average atimes)
	{
		this.atimes = atimes;
	}

	public Average getAdropped()
	{
		return adropped;
	}

	public void setAdropped(Average adropped)
	{
		this.adropped = adropped;
	}

	public int getDroppedTicks()
	{
		return droppedTicks;
	}

	public void setDroppedTicks(int droppedTicks)
	{
		this.droppedTicks = droppedTicks;
	}
}
