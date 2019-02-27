package mortar.util.text;

import java.io.Serializable;

import mortar.bukkit.plugin.MortarAPIPlugin;
import mortar.lang.collection.GList;
import mortar.lang.collection.GMap;

public class D implements Serializable, Logged
{
	private static final long serialVersionUID = 1L;
	private String tag;
	public static final D d = new D("Phantom");
	private static final GMap<String, D> dm = new GMap<>();

	public D(D d, String e)
	{
		this(d.tag + " > " + e);
	}

	public D(String t)
	{
		tag = t;
	}

	private void log(String f, Object... s)
	{
		GList<Object> m = new GList<Object>(s);
		String msg = m.toString(", ");
		String c = C.getLastColors(msg);

		if(f.equals("INFO"))
		{
			c = C.WHITE.toString();
		}

		if(f.equals("WARN"))
		{
			c = C.YELLOW.toString();
		}

		if(f.equals("FATAL"))
		{
			c = C.RED.toString();
		}

		if(f.equals("VERBOSE"))
		{
			c = C.AQUA.toString();
		}

		MortarAPIPlugin.log(c + "|" + f.toUpperCase() + "| " + C.LIGHT_PURPLE + tag + C.WHITE + " " + c + msg + c);
	}

	public static D as(Object o)
	{
		return as(o.getClass().getSimpleName());
	}

	public static D as(String tag)
	{
		if(!dm.containsKey(tag))
		{
			dm.put(tag, new D(tag));
		}

		return dm.get(tag);
	}

	@Override
	public void l(Object... s)
	{
		log("INFO", s);
	}

	public void v(Object... s)
	{
		log("VERBOSE", s);
	}

	@Override
	public void w(Object... s)
	{
		log("WARN", s);
	}

	@Override
	public void f(Object... s)
	{
		log("FATAL", s);
	}

	public static void ll(Object... s)
	{
		d.log("INFO", s);
	}

	public static void vv(Object... s)
	{
		d.log("VERBOSE", s);
	}

	public static void ww(Object... s)
	{
		d.log("WARN", s);
	}

	public static void ff(Object... s)
	{
		d.log("FATAL", s);
	}
}
