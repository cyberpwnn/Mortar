package mortar.api.fulcrum;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

public class ShittyWebserver
{
	private int port;
	private File root;
	private Server server;

	public ShittyWebserver(int port, File root)
	{
		this.port = port;
		this.root = root;
		root.mkdirs();
	}

	public void start() throws Exception
	{
		System.out.println("Spinning up fcu webserver on *:" + port + " hosting root " + root.getAbsolutePath());
		server = new Server(port);
		ResourceHandler resource_handler = new ResourceHandler();
		resource_handler.setDirectoriesListed(true);
		resource_handler.setWelcomeFiles(new String[] {"index.html"});
		resource_handler.setResourceBase(root.getAbsolutePath());

		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] {resource_handler, new DefaultHandler()});
		server.setHandler(handlers);
		server.start();

		try
		{
			writeResource(Fulcrum.class.getResource("/web/index.html"), new File(root, "index.html"));
			writeResource(Fulcrum.class.getResource("/web/smalllogo.png"), new File(root, "smalllogo.png"));
			writeResource(Fulcrum.class.getResource("/web/canvas.js"), new File(root, "canvas.js"));
			writeResource(Fulcrum.class.getResource("/web/index.js"), new File(root, "index.js"));
		}

		catch(Exception e)
		{

		}
	}

	public void stop()
	{
		System.out.println("Shutting down shitty webserver.");
		try
		{
			server.stop();
		}

		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private void writeResource(URL url, File f) throws IOException
	{
		f.createNewFile();
		FileOutputStream fos = new FileOutputStream(f);
		InputStream in = url.openStream();
		byte[] buffer = new byte[1024];
		int read = 0;

		while((read = in.read(buffer)) != -1)
		{
			fos.write(buffer, 0, read);
		}

		fos.close();
		in.close();
	}

	public int getPort()
	{
		return port;
	}

	public File getRoot()
	{
		return root;
	}

	public Server getServer()
	{
		return server;
	}
}
