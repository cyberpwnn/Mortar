package mortar.api.fulcrum.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileResource implements IResource
{
	private final File file;

	public FileResource(File file)
	{
		this.file = file;
	}

	@Override
	public InputStream getInputStream()
	{
		try
		{
			return new FileInputStream(file);
		}

		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public String getVirtualPath()
	{
		return file.getPath();
	}
}
