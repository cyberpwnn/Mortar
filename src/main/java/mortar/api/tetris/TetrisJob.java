package mortar.api.tetris;

import java.util.UUID;

public class TetrisJob implements Job
{
	private long scheduledTick;
	private int minimumDelay;
	private final String id;
	private JobUrgency urgency;
	private JobIgnoreCondition ignoreCondition;
	private JobEnvironment environment;
	private Runnable runnable;
	private UUID uuid;

	public TetrisJob(String id)
	{
		this.id = id == null ? "error" : id;
		this.minimumDelay = 0;
		this.urgency = JobUrgency.NORMAL;
		this.ignoreCondition = JobIgnoreCondition.NEVER;
		this.environment = JobEnvironment.AYNCHRONOUS;
		this.uuid = UUID.randomUUID();
		this.scheduledTick = -1;
		runnable = () ->
		{
		};
	}

	@Override
	public int getMinimumDelay()
	{
		return minimumDelay;
	}

	@Override
	public String getID()
	{
		if(id == null)
		{
			return "null";
		}

		return id;
	}

	@Override
	public JobUrgency getUrgency()
	{
		return urgency;
	}

	@Override
	public void setMinimumDelay(int minimumDelay)
	{
		this.minimumDelay = minimumDelay;
	}

	@Override
	public void setUrgency(JobUrgency urgency)
	{
		this.urgency = urgency;
	}

	@Override
	public JobIgnoreCondition getIgnoreCondition()
	{
		return ignoreCondition;
	}

	@Override
	public void setIgnoreCondition(JobIgnoreCondition ignoreCondition)
	{
		this.ignoreCondition = ignoreCondition;
	}

	@Override
	public JobEnvironment getEnvironment()
	{
		return environment;
	}

	@Override
	public void setJobEnvironment(JobEnvironment environment)
	{
		this.environment = environment;
	}

	@Override
	public void setRunnable(Runnable r)
	{
		this.runnable = r;
	}

	@Override
	public Runnable getRunnable()
	{
		return runnable == null ? () ->
		{
		} : runnable;
	}

	@Override
	public UUID getUUID()
	{
		return uuid;
	}

	@Override
	public long getScheduledTick()
	{
		return scheduledTick;
	}

	@Override
	public void setScheduledTick(long tick)
	{
		this.scheduledTick = tick;
	}
}
