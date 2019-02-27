package mortar.compute.math;

public class Average extends RollingAverage
{
	public Average(int size)
	{
		super(size);
	}

	public double getAverage()
	{
		return get();
	}
}
