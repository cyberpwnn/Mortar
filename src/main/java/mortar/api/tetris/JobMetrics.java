package mortar.api.tetris;

import mortar.compute.math.CappedAverage;
import mortar.logic.format.F;

public class JobMetrics
{
	private CappedAverage time;

	public JobMetrics()
	{
		time = new CappedAverage(32, 4);

		for(int i = 0; i < 32; i++)
		{
			time.put(0.01);
		}
	}

	public void log(double time)
	{
		this.time.put(time);
	}

	public double getEstimatedComputeTime()
	{
		return (time.getMean() + time.getMax()) / 2D;
	}

	public CappedAverage getTime()
	{
		return time;
	}

	@Override
	public String toString()
	{
		return "Estimated: " + F.time(getEstimatedComputeTime(), 2) + " Range: " + F.time(time.getMin(), 2) + " - " + F.time(time.getMax(), 2) + " Mean: " + F.time(time.getMean(), 2) + " AVG: " + F.time(time.getAverage(), 2);
	}
}
