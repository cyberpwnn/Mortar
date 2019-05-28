package mortar.api.tetris;

/**
 * Determines when the job scheduler is allowed to skip execution on this job
 * (cancelling it).
 *
 * @author cyberpwn
 *
 */
public enum JobIgnoreCondition
{
	/**
	 * This job is guaranteed to run at some point. It must be run, it cannot be
	 * cancelled.
	 */
	NEVER,

	/**
	 * If a newer job with the same ID is scheduled later, and this job has not yet
	 * been run, the job scheduler is allowed to cancel this job. This wont always
	 * happen. This will only happen if there is other higher priority jobs.
	 */
	IF_NEWER_EXISTS,

	/**
	 * If other jobs with higher priorities take up the entire pool, this job is
	 * allowed to be cancelled. It is also allowed to be cancelled if a newer job
	 * with the same id is scheduled and there isnt enough time to execute both.
	 */
	ANYTIME;
}
