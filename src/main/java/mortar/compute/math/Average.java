package mortar.compute.math;

import java.util.Collections;

import mortar.lang.collection.GList;

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

	public double getMean()
	{
		GList<Double> g = new GList<>(data);
		Collections.sort(g);

		while(g.size() > 2)
		{
			g.pop();
			g.popLast();
		}

		if(g.size() > 1)
		{
			return (g.get(0) + g.get(1)) / 2D;
		}

		return g.get(0);
	}

	public double getMin()
	{
		double v = Double.MAX_VALUE;

		for(double i : data)
		{
			if(i < v)
			{
				v = i;
			}
		}

		return v;
	}

	public double getMax()
	{
		double v = Double.MIN_VALUE;

		for(double i : data)
		{
			if(i > v)
			{
				v = i;
			}
		}

		return v;
	}
}
