package mortar.api.sound;

public class MFAFilter implements AudioFilter
{
	private float min;
	private float max;
	private int spread;
	private float volume;

	public MFAFilter()
	{
		min = 0.1f;
		max = 1.9f;
		spread = 4;
		volume = 0.5f;
	}

	public MFAFilter scale(float min, float max)
	{
		this.min = min;
		this.max = max;
		return this;
	}

	public MFAFilter spread(int spread)
	{
		this.spread = spread;
		return this;
	}

	public MFAFilter volume(float v)
	{
		this.volume = v;
		return this;
	}

	@Override
	public Audible apply(Audible a)
	{
		Audible n = new Audio();
		float span = max - min;
		float step = span / (float) spread;

		for(int i = 0; i < spread; i++)
		{
			Audible ax = ((Audio) a).clone();
			ax.scalePitch((step * i) + min);
			ax.scaleVolume(volume);
			n.addChild(ax);
		}

		return n;
	}
}
