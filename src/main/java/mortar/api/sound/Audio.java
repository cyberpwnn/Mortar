package mortar.api.sound;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import mortar.api.sched.S;
import mortar.lang.collection.GList;

public class Audio implements Audible
{
	private float v;
	private float p;
	private SoundCategory c;
	private Sound s;
	private GList<Audible> a;
	private int delay;
	private String sound;

	public Audio()
	{
		sound = null;
		a = new GList<Audible>();
		c(SoundCategory.AMBIENT).v(1f).p(1f);
		delay = 0;
	}

	public Audio(Audible ax)
	{
		v = ax.getVolume();
		p = ax.getPitch();
		c = ax.getCategory();
		s = ax.getSound();
		a = ax.getChildren().copy();
		delay = ax.getDelay();
		sound = ax.getSoundString();

	}

	@Override
	public void play(Location l, float v, float p)
	{
		setVolume(v);
		setPitch(p);
		play(l);

		for(Audible i : getChildren())
		{
			i.play(l, v, p);
		}
	}

	@Override
	public void play(Player l, float v, float p)
	{
		setVolume(v);
		setPitch(p);
		play(l);

		for(Audible i : getChildren())
		{
			i.play(l, v, p);
		}
	}

	@Override
	public void play(Location l)
	{
		if(hasDelay())
		{
			new S(getDelay())
			{
				@Override
				public void run()
				{
					if(sound != null)
					{
						l.getWorld().playSound(l, getSoundString(), getVolume(), getPitch());
					}

					else
					{
						l.getWorld().playSound(l, getSound(), getVolume(), getPitch());
					}

					for(Audible i : getChildren())
					{
						i.play(l);
					}
				}
			};
		}

		else
		{
			if(sound != null)
			{
				l.getWorld().playSound(l, getSoundString(), getCategory(), getVolume(), getPitch());
			}

			else
			{
				l.getWorld().playSound(l, getSound(), getVolume(), getPitch());
			}

			for(Audible i : getChildren())
			{
				i.play(l);
			}
		}
	}

	@Override
	public void play(Player l, Location pos)
	{
		if(hasDelay())
		{
			new S(getDelay())
			{
				@Override
				public void run()
				{
					if(sound != null)
					{
						l.playSound(pos, getSoundString(), getVolume(), getPitch());
					}

					else
					{
						l.playSound(pos, getSound(), getVolume(), getPitch());
					}

					for(Audible i : getChildren())
					{
						i.play(l, pos);
					}
				}
			};
		}

		else
		{
			if(sound != null)
			{
				l.playSound(pos, getSoundString(), getVolume(), getPitch());
			}

			else
			{
				l.playSound(pos, getSound(), getVolume(), getPitch());
			}

			for(Audible i : getChildren())
			{
				i.play(l, pos);
			}
		}
	}

	@Override
	public void play(Player l)
	{
		play(l, l.getLocation());
	}

	@Override
	public Audible setVolume(float v)
	{
		this.v = v;
		return this;
	}

	@Override
	public Audible setPitch(float p)
	{
		this.p = p;
		return this;
	}

	@Override
	public float getVolume()
	{
		return v;
	}

	@Override
	public float getPitch()
	{
		return p;
	}

	@Override
	public GList<Audible> getChildren()
	{
		return a;
	}

	@Override
	public Audible addChild(Audible a)
	{
		this.a.add(a);
		return this;
	}

	@Override
	public SoundCategory getCategory()
	{
		return c;
	}

	@Override
	public Audible setCategory(SoundCategory c)
	{
		this.c = c;
		return this;
	}

	@Override
	public Sound getSound()
	{
		return s;
	}

	@Override
	public Audible setSound(Sound s)
	{
		this.s = s;
		return this;
	}

	@Override
	public Audible v(float v)
	{
		return setVolume(v);
	}

	@Override
	public Audible p(float p)
	{
		return setPitch(p);
	}

	@Override
	public Audible c(SoundCategory c)
	{
		return setCategory(c);
	}

	@Override
	public Audible s(Sound s)
	{
		return setSound(s);
	}

	@Override
	public Audible vp(float v, float p)
	{
		return v(v).p(p);
	}

	@Override
	public void scalePitch(float p)
	{
		setPitch((getPitch() + p) / 2f);

		for(Audible i : getChildren())
		{
			i.scalePitch(p);
		}
	}

	@Override
	public void scaleVolume(float p)
	{
		setVolume((getVolume() + p) / 2f);

		for(Audible i : getChildren())
		{
			i.scaleVolume(p);
		}
	}

	@Override
	public Audio clone()
	{
		return (Audio) new Audio().d(this.delay).v(this.v).p(this.p).s(this.s).c(this.c).addChildren(this.getChildren());
	}

	@Override
	public Audible addChildren(GList<Audible> a)
	{
		getChildren().addAll(a);
		return this;
	}

	@Override
	public Audible setDelay(int ticks)
	{
		this.delay = ticks;
		return this;
	}

	@Override
	public int getDelay()
	{
		return delay;
	}

	@Override
	public Audible d(int ticks)
	{
		return setDelay(ticks);
	}

	@Override
	public boolean hasDelay()
	{
		return getDelay() > 0;
	}

	@Override
	public Audible s(String s)
	{
		sound = s;
		return this;
	}

	@Override
	public String getSoundString()
	{
		return sound;
	}

	@Override
	public Audible setSound(String s)
	{
		sound = s;
		return this;
	}

	@Override
	public Audible osc(double d)
	{
		return new Audio(this).p((float) (Math.sin(Math.random()) * d));
	}
}
