package mortar.api.tetris;

public enum JobEnvironment
{
	/**
	 * Require the job to run on the main server thread
	 */
	SYNCHRONOUS,

	/**
	 * Require the job to run on an async thread. The job scheduler may run this
	 * task in parallel with other tasks or with other tasks with the same id
	 */
	AYNCHRONOUS;
}
