package mortar.util.queue;

import mortar.compute.math.M;

public abstract class ThrottledExecutor<T> extends PhantomExecutor<T>
{
	@Override
	public void doUpdate()
	{
		int size = getQueue().size();
		int allow = M.iclip((int) ((double) size / 1.125D), 2, 250);

		while(getQueue().hasNext() && allow > 0)
		{
			allow--;
			execute(getQueue().next());
		}
	}
}
