package mortar.api.fulcrum.object;

import java.io.IOException;

import mortar.api.fulcrum.FulcrumInstance;
import mortar.logic.io.VIO;

public class FCUTextResource extends FCUResource
{
	public FCUTextResource(String id, String text)
	{
		super(id, FulcrumInstance.instance.getResources().cacheResourceText(id, text));
	}

	public FCUTextResource(String id, Class<?> anchor, String resource)
	{
		super(id, anchor, resource);
	}

	public void rewrite(String string, String idx)
	{
		try
		{
			updateCacheKey(FulcrumInstance.instance.getResources().cacheResourceText(this.getID(), VIO.readAll(stream()).replaceAll("\\Q" + string + "\\E", idx)));
		}

		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}