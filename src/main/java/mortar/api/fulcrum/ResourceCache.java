package mortar.api.fulcrum;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import mortar.api.fulcrum.util.IResource;
import mortar.logic.io.VIO;

public class ResourceCache
{
	private File cacheDir;

	public ResourceCache(String id)
	{
		this.cacheDir = new File("cache", id);
		cacheDir.mkdirs();
	}

	public File fileFor(String name)
	{
		return new File(cacheDir, name);
	}

	public boolean hasCachedResource(Class<?> anchor, String resource)
	{
		if(!resource.startsWith("/"))
		{
			resource = "/" + resource;
		}

		String codeSource = new File(anchor.getProtectionDomain().getCodeSource().getLocation().getFile()).getName();
		String cacheKey = codeSource + resource;
		return fileFor(cacheKey).exists();
	}

	public InputStream getCachedResource(Class<?> anchor, String resource)
	{
		if(!resource.startsWith("/"))
		{
			resource = "/" + resource;
		}

		String codeSource = new File(anchor.getProtectionDomain().getCodeSource().getLocation().getFile()).getName();
		String cacheKey = codeSource + resource;
		File f = fileFor(cacheKey);

		if(f.exists())
		{
			try
			{
				return new FileInputStream(f);
			}

			catch(Throwable e)
			{

			}
		}

		return null;
	}

	public InputStream getCachedResource(String cacheKey)
	{
		File f = fileFor(cacheKey);

		if(f.exists())
		{
			try
			{
				return new FileInputStream(f);
			}

			catch(Throwable e)
			{

			}
		}

		return null;
	}

	public InputStream cacheAndGetResource(Class<?> anchor, String resource)
	{
		if(!resource.startsWith("/"))
		{
			resource = "/" + resource;
		}

		String codeSource = new File(anchor.getProtectionDomain().getCodeSource().getLocation().getFile()).getName();
		String cacheKey = codeSource + resource;
		File f = fileFor(cacheKey);
		f.getParentFile().mkdirs();

		try
		{
			InputStream in = anchor.getResourceAsStream(resource);
			FileOutputStream fos = new FileOutputStream(f);
			VIO.fullTransfer(in, fos, 16819);
			in.close();
			fos.close();
		}

		catch(Throwable e)
		{

		}

		if(f.exists())
		{
			try
			{
				return new FileInputStream(f);
			}

			catch(Throwable e)
			{

			}
		}

		return null;
	}

	@SuppressWarnings("deprecation")
	public URL cacheResourceURL(Class<?> anchor, String resource)
	{
		String key = cacheResource(anchor, resource);

		if(key == null)
		{
			return null;
		}

		try
		{
			return fileFor(key).toURL();
		}

		catch(MalformedURLException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public String cacheResource(IResource r)
	{
		String cacheKey = "res/" + r.getVirtualPath();
		File f = fileFor(cacheKey);
		f.getParentFile().mkdirs();

		try
		{
			InputStream in = r.getInputStream();
			FileOutputStream fos = new FileOutputStream(f);
			VIO.fullTransfer(in, fos, 16819);
			in.close();
			fos.close();
		}

		catch(Throwable e)
		{

		}

		if(f.exists())
		{
			try
			{
				return cacheKey;
			}

			catch(Throwable e)
			{

			}
		}

		return null;
	}

	public String cacheResource(Class<?> anchor, String resource)
	{
		if(!resource.startsWith("/"))
		{
			resource = "/" + resource;
		}

		String codeSource = new File(anchor.getProtectionDomain().getCodeSource().getLocation().getFile()).getName();
		String cacheKey = codeSource + resource;
		File f = fileFor(cacheKey);
		f.getParentFile().mkdirs();

		try
		{
			InputStream in = anchor.getResourceAsStream(resource);
			FileOutputStream fos = new FileOutputStream(f);
			VIO.fullTransfer(in, fos, 16819);
			in.close();
			fos.close();
		}

		catch(Throwable e)
		{

		}

		if(f.exists())
		{
			try
			{
				return cacheKey;
			}

			catch(Throwable e)
			{

			}
		}

		return null;
	}

	public String cacheResourceText(String id, String text)
	{
		String cacheKey = "text/" + id + ".txt";
		File f = fileFor(cacheKey);
		f.getParentFile().mkdirs();

		try
		{
			VIO.writeAll(f, text);
		}

		catch(Throwable e)
		{

		}

		if(f.exists())
		{
			try
			{
				return cacheKey;
			}

			catch(Throwable e)
			{

			}
		}

		return null;
	}
}
