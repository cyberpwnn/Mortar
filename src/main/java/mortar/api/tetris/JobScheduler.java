package mortar.api.tetris;

import mortar.lang.collection.Callback;

public interface JobScheduler
{
	public static JobScheduler scheduler = new TetrisJobScheduler();

	public void tick();

	public void tock();

	public void notifyJobResult(Job job, JobResult result);

	/**
	 * Get / create-get job metrics for a given job id
	 *
	 * @param jobID
	 *            the job id
	 * @return the job metrics
	 */
	public JobMetrics getMetrics(String jobID);

	/**
	 * Clear job metrics on the given job id
	 *
	 * @param jobID
	 *            the job id
	 */
	public void clearMetrics(String jobID);

	/**
	 * Sleeps on current thread for a job to finish and return a result.
	 *
	 * @param job
	 *            the job
	 * @return the result
	 */
	public JobResult scheduleBlocking(Job job);

	/**
	 * Just run the job dammit. I dont care what happens to it
	 *
	 * @param job
	 *            the job to run
	 */
	public void scheduleBlindly(Job job);

	/**
	 * Schedule a job to be run
	 *
	 * @param job
	 *            the job
	 * @param resultCallback
	 *            the result, could have been executed or cancelled before it was
	 *            executed
	 */
	public void schedule(Job job, Callback<JobResult> resultCallback);

	/**
	 * Complete all jobs now and back-queue new scheduled jobs while 'completing'
	 *
	 */
	public void completeNow();
}
