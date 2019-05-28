package mortar.api.tetris;

public class JobResult
{
	private double timeSpent;
	private double latency;
	private JobResultStatus status;

	public JobResult(double timeSpent, double latency, JobResultStatus status)
	{
		this.timeSpent = timeSpent;
		this.latency = latency;
		this.status = status;
	}

	public double getTimeSpent()
	{
		return timeSpent;
	}

	public void setTimeSpent(double timeSpent)
	{
		this.timeSpent = timeSpent;
	}

	public double getLatency()
	{
		return latency;
	}

	public void setLatency(double latency)
	{
		this.latency = latency;
	}

	public JobResultStatus getStatus()
	{
		return status;
	}

	public void setStatus(JobResultStatus status)
	{
		this.status = status;
	}
}
