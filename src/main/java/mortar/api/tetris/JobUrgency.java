package mortar.api.tetris;

public enum JobUrgency
{
	/**
	 * s Urgent tasks are executed as if they are urgent. Everything else is put
	 * before them.
	 */
	URGENT(true),

	/**
	 * Higher than normal priority, these are very reliable and should be used
	 * sparingly.
	 */
	HIGH,

	/**
	 * The priority that bukkit scheduled tasks through mortar use.
	 */
	NORMAL,

	/**
	 * Prefer to execute, but after the more important stuff. Considered lower
	 * priority than normal tasks.
	 */
	LOW,

	/**
	 * This is so unimportant that it could take over a minute for this to finally
	 * execute.
	 */
	LAZY;

	private boolean ignoresOccupancyLimits;

	private JobUrgency()
	{
		this(false);
	}

	private JobUrgency(boolean ignoresOccupancyLimits)
	{
		this.ignoresOccupancyLimits = ignoresOccupancyLimits;
	}

	public boolean ignoresOccupancyLimits()
	{
		return ignoresOccupancyLimits;
	}
}
