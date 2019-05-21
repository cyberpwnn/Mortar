package mortar.api.resourcepack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.bukkit.Bukkit;

import com.googlecode.pngtastic.core.PngImage;
import com.googlecode.pngtastic.core.PngOptimizer;

import mortar.api.sched.S;
import mortar.compute.math.M;
import mortar.lang.collection.GList;
import mortar.lang.collection.GMap;
import mortar.lang.json.JSONObject;
import mortar.logic.format.F;
import mortar.logic.io.VIO;
import mortar.util.text.C;
import mortar.util.text.TXT;

public class ResourcePack
{
	private long totalSaved = 0;
	private final PackMeta meta;
	private GMap<String, URL> copyResources;
	private GMap<String, String> writeResources;
	GList<String> oc = new GList<String>();
	GList<String> ow = new GList<String>();
	private boolean optimizePngs;
	private boolean minifyJSON = false;
	private boolean overbose = false;
	private boolean obfuscate = false;

	public void o(String s)
	{
		if(!overbose)
		{
			return;
		}

		new S()
		{
			@Override
			public void run()
			{
				Bukkit.getConsoleSender().sendMessage(TXT.makeTag(C.RED, C.WHITE, C.GRAY, "FU OVERBOSE") + s);
			}
		};
	}

	public void f(String s)
	{
		new S()
		{
			@Override
			public void run()
			{
				Bukkit.getConsoleSender().sendMessage(TXT.makeTag(C.RED, C.WHITE, C.RED, "FU ERROR") + s);
			}
		};
	}

	public ResourcePack()
	{
		optimizePngs = false;
		meta = new PackMeta();
		copyResources = new GMap<String, URL>();
		writeResources = new GMap<String, String>();
		oc = new GList<String>();
		ow = new GList<String>();
	}

	public boolean isOptimizedPngs()
	{
		return optimizePngs;
	}

	public void setOptimizePngs(boolean optimize)
	{
		this.optimizePngs = optimize;
	}

	public int size()
	{
		return copyResources.size() + writeResources.size() + 3;
	}

	public void removeResource(String path)
	{
		oc.remove(path);
		copyResources.remove(path);
		writeResources.remove(path);
	}

	public void setResource(String path, URL url)
	{
		if(path == null && url == null)
		{
			return;
		}

		if(path == null)
		{
			f("PATH IS NULL: " + url.toString());
			return;
		}

		if(url == null)
		{
			f("URL IS NULL: " + path);
			return;
		}

		oc.add(path);
		copyResources.put(path, url);

		try
		{
			o("Adding Resource: " + C.WHITE + path + C.GRAY + " from url " + C.WHITE + url.getFile().split("\\Q!\\E")[1]);
		}

		catch(Exception e)
		{
			o("Adding Resource: " + C.WHITE + path + C.GRAY + " from url " + C.WHITE + url.getFile());
		}
	}

	public void setResource(String path, String content)
	{
		ow.add(path);
		writeResources.put(path, content);

		o("Adding Resource: " + C.WHITE + path + C.GRAY + " from TEXT " + C.WHITE + (content.length() > 40 ? content.substring(0, 40) + "..." : content).replaceAll("\n", ""));
	}

	public PackMeta getMeta()
	{
		return meta;
	}

	public byte[] writeToArchive(File f) throws IOException, NoSuchAlgorithmException
	{
		File fx = new File(f.getParentFile(), f.getName() + "-gen");
		if(fx.exists())
		{
			VIO.delete(fx);
		}

		fx.mkdirs();
		writeToFolder(fx);
		f.createNewFile();

		MessageDigest d = MessageDigest.getInstance("MD5");
		FileOutputStream fos = new FileOutputStream(f);
		ZipOutputStream zos = new ZipOutputStream(fos);

		for(File i : fx.listFiles())
		{
			addToZip(d, i, fx, zos);
		}

		zos.close();
		f.setLastModified(M.ms());

		return d.digest();
	}

	private void addToZip(MessageDigest d, File file, File root, ZipOutputStream s) throws IOException
	{
		if(file.isDirectory())
		{
			for(File i : file.listFiles())
			{
				addToZip(d, i, root, s);
			}
		}

		else
		{
			String path = file.getAbsolutePath();
			String base = root.getAbsolutePath();
			String relative = new File(base).toURI().relativize(new File(path).toURI()).getPath();
			ZipEntry ze = new ZipEntry(relative);
			FileInputStream fin = new FileInputStream(file);
			o("Zipping " + C.WHITE + file.getPath());
			s.putNextEntry(ze);
			byte[] buf = new byte[1024];
			int read = 0;

			while((read = fin.read(buf)) != -1)
			{
				d.update(buf, 0, read);
				s.write(buf, 0, read);
			}

			s.closeEntry();
			fin.close();
		}
	}

	public void writeToFolder(File f) throws IOException
	{
		f.mkdirs();
		totalSaved = 0;
		writePackContent(new File(f, "pack.mcmeta"), getMeta().toString());
		writeResourceToFile(getMeta().getPackIcon(), new File(f, "pack.png"));
		GList<File> jsonFiles = new GList<>();
		GList<File> allFiles = new GList<>();

		for(String i : oc.copy())
		{
			File destination = new File(f, "assets" + File.separator + "minecraft" + File.separator + i.replaceAll("/", Matcher.quoteReplacement(File.separator)));
			destination.getParentFile().mkdirs();
			writeResourceToFile(copyResources.get(i), destination);

			if(destination.getName().endsWith(".json"))
			{
				jsonFiles.add(destination);
			}

			allFiles.add(destination);
		}

		for(String i : ow.copy())
		{
			File destination = new File(f, "assets" + File.separator + "minecraft" + File.separator + i.replaceAll("/", Matcher.quoteReplacement(File.separator)));
			destination.getParentFile().mkdirs();
			writePackContent(destination, writeResources.get(i));

			if(destination.getName().endsWith(".json"))
			{
				jsonFiles.add(destination);
			}

			allFiles.add(destination);
		}

		for(File i : jsonFiles)
		{
			try
			{
				JSONObject o = new JSONObject(VIO.readAll(i));
				VIO.writeAll(i, o.toString(minifyJSON ? 0 : 4));
			}

			catch(Throwable e)
			{

			}
		}

		if(isOptimizedPngs())
		{
			o("Saved a total of " + F.ofSize(totalSaved, 1024, 2));
		}
	}

	private void writeResourceToFile(URL url, File f) throws IOException
	{
		if(url == null)
		{
			f("WARNING! Resource is null: " + f.getAbsolutePath());
			return;
		}

		try
		{
			try
			{
				o("Writing " + C.WHITE + url.getFile().split("\\Q!\\E")[1] + C.GRAY + " to " + C.WHITE + f.getPath());
			}

			catch(Exception e)
			{
				o("Writing " + C.WHITE + url.getFile() + C.GRAY + " to " + C.WHITE + f.getPath());
			}

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

			if(f.getName().endsWith(".png") && isOptimizedPngs())
			{
				optimizePNG(f);
			}
		}

		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("FAILED TO PACK RESOURCE: " + e.getMessage());
		}
	}

	private void optimizePNG(File f) throws IOException
	{
		PngOptimizer o = new PngOptimizer();
		PngImage img = new PngImage(f.getPath(), "NONE");
		o.setCompressor("zopfli", 32);
		o.optimize(img, f.getPath(), true, 9);
		long sa = o.getTotalSavings();
		o("Optimized " + f.getName() + " (saved " + F.fileSize(sa) + ")");
		totalSaved += sa;
	}

	private void writePackContent(File m, String content) throws IOException
	{
		o("Writing " + C.WHITE + (content.length() > 40 ? content.substring(0, 40) + "..." : content).replaceAll("\n", "") + C.GRAY + " to " + C.WHITE + m.getPath());
		m.createNewFile();
		PrintWriter pw = new PrintWriter(m);
		pw.println(content);
		pw.close();
	}

	public void setOverbose(boolean hasFlag)
	{
		overbose = hasFlag;
	}

	public void setMinifyJSON(boolean minifyJSON)
	{
		this.minifyJSON = minifyJSON;
	}

	public void setObfuscate(boolean obfuscate)
	{
		this.obfuscate = obfuscate;
	}
}
