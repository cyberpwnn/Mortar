package mortar.api.tetris;

import java.util.UUID;

public interface Job
{
	/**
	 * Get the tick this job was scheduled at
	 *
	 * @return the tick
	 */
	public long getScheduledTick();

	/**
	 * Set the tick this job was scheduled at
	 *
	 * @param tick
	 *            the tick
	 */
	public void setScheduledTick(long tick);

	/**
	 * Get this job's unique id
	 *
	 * @return the id
	 */
	public UUID getUUID();

	/**
	 * Set the runnable for this job
	 *
	 * @param r
	 *            the runnable
	 */
	public void setRunnable(Runnable r);

	/**
	 * Get the runnable for this job
	 *
	 * @return the runnable
	 */
	public Runnable getRunnable();

	/**
	 * Get the job environment (defaults to async)
	 *
	 * @return the environment
	 */
	public JobEnvironment getEnvironment();

	/**
	 * Set the job environment (sync/async)
	 *
	 * @param environment
	 *            the env
	 */
	public void setJobEnvironment(JobEnvironment environment);

	/**
	 * Get the minimum delay before this job can be executed. This is essentially a
	 * delay for the task, however it is not exact. If the delay is set to 5 ticks,
	 * the task will be executed after 5-? ticks. But never below 5 ticks.
	 *
	 * @return the minimum delay in ticks.
	 */
	public int getMinimumDelay();

	/**
	 * Get the ID for this task
	 *
	 * @return the task id.
	 */
	public String getID();

	/**
	 * Get the urgency for this job.
	 *
	 * @return the urgency
	 */
	public JobUrgency getUrgency();

	/**
	 * the minimum delay before this job can be executed. This is essentially a
	 * delay for the task, however it is not exact. If the delay is set to 5 ticks,
	 * the task will be executed after 5-? ticks. But never below 5 ticks.
	 *
	 * @param minimumDelay
	 *            the delay in ticks.
	 */
	public void setMinimumDelay(int minimumDelay);

	/**
	 * Set the urgency for this job. It is usually best to keep it at normal
	 *
	 * @param urgency
	 *            the urgency
	 */
	public void setUrgency(JobUrgency urgency);

	/**
	 * Get the condition which the job scheduler is allowed to cancel your job from
	 * executing.
	 *
	 * @return the ignore condition
	 */
	public JobIgnoreCondition getIgnoreCondition();

	/**
	 * Set the condition which the job scheduler is allowed to cancel your job from
	 * executing.
	 *
	 * @param ignoreCondition
	 *            the ignore condition.
	 */
	public void setIgnoreCondition(JobIgnoreCondition ignoreCondition);

	public static JobBuilder schedule(String jobID)
	{
		return new JobBuilder(jobID);
	}
}
