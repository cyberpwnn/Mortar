package mortar.api.fulcrum.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.zeroturnaround.zip.ZipUtil;

import mortar.logic.io.VIO;
import mortar.util.text.D;

public class SuperCacheResourceProvider implements IResourceProvider
{
	private File resources;

	public SuperCacheResourceProvider(File resources)
	{
		this.resources = resources;
		resources.mkdirs();
		cacheAllResources();
	}

	public void cacheAllResources()
	{
		D.as(this).l("Caching Resources");
		for(File i : new File("plugins").listFiles())
		{
			if(i.isFile() && i.getName().endsWith(".jar"))
			{
				cacheResourcesInJar(i);
			}
		}
	}

	public void cacheResourcesInJar(File jar)
	{
		if(ZipUtil.containsEntry(jar, "assets"))
		{
			ZipUtil.unpack(jar, getResourcesFile());
			D.as(this).l("Cached Assets from jar " + jar.getName());
			for(File i : getResourcesFile().listFiles())
			{
				if(!(i.isDirectory() && i.getName().equals("assets")))
				{
					VIO.delete(i);
				}
			}
		}
	}

	public void cacheResouce(IResource r) throws IOException
	{
		File f = new File(getResourcesFile(), r.getVirtualPath());
		f.getParentFile().mkdirs();
		InputStream in = r.getInputStream();
		FileOutputStream fos = new FileOutputStream(f);
		VIO.fullTransfer(in, fos, 8192);
	}

	public File getResourcesFile()
	{
		return resources;
	}

	@Override
	public IResource get(String path)
	{
		if(path.startsWith("/"))
		{
			path = path.substring(1);
		}

		File f = new File(getResourcesFile(), path);

		if(f.exists())
		{
			return new FileResource(f);
		}

		return null;
	}

}
