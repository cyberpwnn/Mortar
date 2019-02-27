package mortar.util.queue;

import mortar.lang.collection.GList;

public class PhantomQueue<T> implements Queue<T>
{
	private GList<T> queue;
	private boolean randomPop;
	private boolean reversePop;

	public PhantomQueue()
	{
		clear();
	}

	public PhantomQueue<T> responsiveMode()
	{
		reversePop = true;
		return this;
	}

	public PhantomQueue<T> randomMode()
	{
		randomPop = true;
		return this;
	}

	@Override
	public void queue(T t)
	{
		queue.add(t);
	}

	@Override
	public void queue(GList<T> t)
	{
		for(T i : t)
		{
			queue(i);
		}
	}

	@Override
	public boolean hasNext(int amt)
	{
		return queue.size() >= amt;
	}

	@Override
	public boolean hasNext()
	{
		return !queue.isEmpty();
	}

	@Override
	public T next()
	{
		return reversePop ? queue.popLast() : randomPop ? queue.popRandom() : queue.pop();
	}

	@Override
	public GList<T> next(int amt)
	{
		GList<T> t = new GList<>();

		for(int i = 0; i < amt; i++)
		{
			if(!hasNext())
			{
				break;
			}

			t.add(next());
		}

		return t;
	}

	@Override
	public void clear()
	{
		queue = new GList<T>();
	}

	@Override
	public int size()
	{
		return queue.size();
	}
}
