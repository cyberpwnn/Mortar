package mortar.api.tetris;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.RejectedExecutionException;

import mortar.compute.math.Profiler;
import mortar.compute.math.RollingAverage;
import mortar.lang.collection.Callback;
import mortar.lang.collection.GMap;
import mortar.util.queue.PhantomQueue;
import mortar.util.queue.Queue;
import mortar.util.text.D;

public class TetrisJobScheduler implements JobScheduler
{
	private GMap<UUID, Callback<JobResult>> callbacks;
	private GMap<String, JobMetrics> metrics;
	private GMap<JobEnvironment, GMap<JobUrgency, Queue<Job>>> queue;
	private Queue<Job> syncQueue;
	private Queue<Runnable> syncBackingQueue;
	private Queue<Job> asyncQueue;
	private Queue<Runnable> asyncBackingQueue;
	private Queue<Runnable> logicQueue;
	private ExecutorService workExecutor;
	private ExecutorService logicExecutor;
	private double threshold;
	private int lastSyncQueue;
	private RollingAverage queueSlope;
	private RollingAverage syncUsage;

	public TetrisJobScheduler()
	{
		queueSlope = new RollingAverage(20);
		syncUsage = new RollingAverage(10);
		syncUsage.put(0);
		queueSlope.put(0);
		lastSyncQueue = 0;
		callbacks = new GMap<>();
		metrics = new GMap<>();
		syncBackingQueue = new PhantomQueue<>();
		asyncBackingQueue = new PhantomQueue<>();
		logicQueue = new PhantomQueue<>();
		syncQueue = new PhantomQueue<>();
		asyncQueue = new PhantomQueue<>();
		queue = new GMap<>();
		threshold = 0.01;

		for(JobEnvironment i : JobEnvironment.values())
		{
			GMap<JobUrgency, Queue<Job>> g = new GMap<>();

			for(JobUrgency j : JobUrgency.values())
			{
				g.put(j, new PhantomQueue<>());
			}

			queue.put(i, g);
		}

		workExecutor = new ForkJoinPool(16, new ForkJoinWorkerThreadFactory()
		{
			@Override
			public ForkJoinWorkerThread newThread(ForkJoinPool pool)
			{
				final ForkJoinWorkerThread worker = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
				worker.setName("Tetris Executor " + worker.getPoolIndex());
				return worker;
			}
		}, new UncaughtExceptionHandler()
		{
			@Override
			public void uncaughtException(Thread t, Throwable e)
			{
				D.as("TetrisExecutor").w("Exception in tetris executor thread\n" + e.getMessage() + "\n" + e.toString());
			}
		}, true);

		logicExecutor = new ForkJoinPool(1, new ForkJoinWorkerThreadFactory()
		{
			@Override
			public ForkJoinWorkerThread newThread(ForkJoinPool pool)
			{
				final ForkJoinWorkerThread worker = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
				worker.setName("Tetris Conductor " + worker.getPoolIndex());
				return worker;
			}
		}, new UncaughtExceptionHandler()
		{
			@Override
			public void uncaughtException(Thread t, Throwable e)
			{
				D.as("TetrisConductor").w("Exception in tetris conductor thread\n" + e.getMessage() + "\n" + e.toString());
			}
		}, true);
	}

	@Override
	public void tick()
	{
		for(JobEnvironment i : JobEnvironment.values())
		{
			tick(i, queue.get(i));
		}

		punchAsyncQueue();
		queueAsyncJobs();
		flushLogicQueue();
	}

	@Override
	public void tock()
	{
		Profiler px = new Profiler();
		px.begin();
		while(syncQueue.hasNext())
		{
			try
			{
				Job j = syncQueue.next();

				if(j == null)
				{
					continue;
				}

				Runnable r = j.getRunnable();

				if(r != null)
				{
					r.run();
				}
			}

			catch(Throwable e)
			{
				e.printStackTrace();
			}
		}

		while(syncBackingQueue.hasNext())
		{
			try
			{
				Job j = syncQueue.next();

				if(j == null)
				{
					continue;
				}

				j.getRunnable().run();
			}

			catch(Throwable e)
			{
				e.printStackTrace();
			}
		}

		px.end();
		syncUsage.put(px.getMilliseconds());
		updateThreshold();
	}

	private void updateThreshold()
	{
		int currentSyncQueue = getQueueSize(JobEnvironment.SYNCHRONOUS);
		queueSlope.put(currentSyncQueue - lastSyncQueue);
		lastSyncQueue = currentSyncQueue;

		if(queueSlope.get() > 0.01)
		{
			threshold += (queueSlope.get() / 525D);
		}

		else if(threshold > 0.001 && lastSyncQueue < 100)
		{
			threshold /= 1.004;

			if(threshold < 0.001)
			{
				threshold = 0.001;
			}
		}

		if(threshold > syncUsage.get() * 2.1)
		{
			threshold /= 1.1D;
		}

		if(queueSlope.get() < -6.5)
		{
			threshold /= 1.017;
		}

		if(threshold > 40 && queueSlope.get() < -2)
		{
			threshold /= 1.25;
		}
	}

	public int getQueueSize(JobEnvironment i, JobUrgency e)
	{
		return queue.get(i).get(e).size();
	}

	public int getQueueSize(JobEnvironment e)
	{
		int m = 0;

		for(JobUrgency i : JobUrgency.values())
		{
			m += getQueueSize(e, i);
		}

		return m;
	}

	public int getQueueSize()
	{
		int m = 0;

		for(JobEnvironment i : JobEnvironment.values())
		{
			m += getQueueSize(i);
		}

		return m;
	}

	private void punchAsyncQueue()
	{
		while(asyncBackingQueue.hasNext())
		{
			Runnable j = asyncBackingQueue.next();

			try
			{
				workExecutor.execute(j);
			}

			catch(RejectedExecutionException e)
			{
				asyncBackingQueue.queue(j);
				break;
			}
		}
	}

	private void flushLogicQueue()
	{
		while(logicQueue.hasNext())
		{
			Runnable j = logicQueue.next();

			try
			{
				if(j == null)
				{
					continue;
				}

				logicExecutor.execute(j);
			}

			catch(RejectedExecutionException e)
			{
				logicQueue.queue(j);
				break;
			}
		}
	}

	private void queueAsyncJobs()
	{
		while(asyncQueue.hasNext())
		{
			Job j = asyncQueue.next();

			try
			{
				workExecutor.execute(j.getRunnable());
			}

			catch(RejectedExecutionException e)
			{
				asyncQueue.queue(j);
				break;
			}
		}
	}

	private void tick(JobEnvironment env, GMap<JobUrgency, Queue<Job>> qm)
	{
		if(env.equals(JobEnvironment.SYNCHRONOUS))
		{
			double occupancy = 0;

			queueing: for(JobUrgency i : JobUrgency.values())
			{
				while(i.ignoresOccupancyLimits() ? true : occupancy < getSyncOccupancyLimit())
				{
					double oc = queue(env, qm.get(i));

					if(oc == -1024909)
					{
						break;
					}

					occupancy += oc;

					if(!i.ignoresOccupancyLimits() && occupancy >= getSyncOccupancyLimit())
					{
						break queueing;
					}
				}
			}
		}

		else
		{
			double occupancy = 0;

			queueing: for(JobUrgency i : JobUrgency.values())
			{
				while(true)
				{
					double oc = queue(env, qm.get(i));

					if(oc == -1024909)
					{
						break;
					}

					occupancy += oc;

					if(!i.ignoresOccupancyLimits() && occupancy >= getSyncOccupancyLimit())
					{
						break queueing;
					}
				}
			}
		}
	}

	private double queue(JobEnvironment env, Queue<Job> queue)
	{
		if(queue.hasNext())
		{
			Job job = queue.next();

			if(job == null)
			{
				return -1024909;
			}

			(env.equals(JobEnvironment.SYNCHRONOUS) ? syncQueue : asyncQueue).queue(job);
			return getMetrics(job.getID()).getEstimatedComputeTime();
		}

		return -1024909;
	}

	private double getSyncOccupancyLimit()
	{
		return threshold;
	}

	@Override
	public JobMetrics getMetrics(String jobID)
	{
		if(!metrics.containsKey(jobID))
		{
			JobMetrics m = new JobMetrics();
			metrics.put(jobID, m);
			return m;
		}

		return metrics.get(jobID);
	}

	@Override
	public void clearMetrics(String jobID)
	{
		metrics.remove(jobID);
	}

	@Override
	public JobResult scheduleBlocking(Job job)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void scheduleBlindly(Job job)
	{
		queue.get(job.getEnvironment()).get(job.getUrgency()).queue(job);
	}

	@Override
	public void schedule(Job job, Callback<JobResult> resultCallback)
	{
		callbacks.put(job.getUUID(), resultCallback);
		scheduleBlindly(job);
	}

	@Override
	public void completeNow()
	{
		for(JobEnvironment i : queue.k())
		{
			for(JobUrgency j : queue.get(i).k())
			{
				Queue<Job> q = queue.get(i).get(j);

				while(q.hasNext())
				{
					q.next().getRunnable().run();
				}
			}
		}

		while(syncQueue.hasNext())
		{
			syncQueue.next().getRunnable().run();
		}

		while(asyncQueue.hasNext())
		{
			asyncQueue.next().getRunnable().run();
		}

		while(logicQueue.hasNext())
		{
			logicQueue.next().run();
		}

		while(syncBackingQueue.hasNext())
		{
			syncBackingQueue.next().run();
		}

		while(asyncBackingQueue.hasNext())
		{
			asyncBackingQueue.next().run();
		}
	}

	@Override
	public void notifyJobResult(Job job, JobResult result)
	{
		logicQueue.queue(new Runnable()
		{
			@Override
			public void run()
			{
				if(callbacks.containsKey(job.getUUID()))
				{
					Callback<JobResult> r = callbacks.get(job.getUUID());
					callbacks.remove(job.getUUID());
					(job.getEnvironment().equals(JobEnvironment.SYNCHRONOUS) ? syncBackingQueue : asyncBackingQueue).queue(() -> r.run(result));
				}
			}
		});
	}
}
