package mortar.logic.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import mortar.api.sql.CustomOutputStream;
import mortar.compute.math.M;

public class Hasher
{
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static String hash(String b)
	{
		try
		{
			MessageDigest d = MessageDigest.getInstance("SHA-256");
			return bytesToHex(d.digest(b.getBytes(StandardCharsets.UTF_8)));
		}

		catch(NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}

		return "¯\\_(ツ)_/¯";
	}

	public static String bytesToHex(byte[] bytes)
	{
		char[] hexChars = new char[bytes.length * 2];
		for(int j = 0; j < bytes.length; j++)
		{
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}

		return new String(hexChars).toUpperCase();
	}

	public static String createToken()
	{
		return randomCase(UUID.randomUUID().toString().replaceAll("-", "") + hash(UUID.nameUUIDFromBytes(UUID.randomUUID().toString().getBytes()).toString().replaceAll("-", "")));
	}

	public static String randomCase(String s)
	{
		String ss = "";

		for(char i : s.toLowerCase().toCharArray())
		{
			ss += M.r(0.5) ? Character.toUpperCase(i) : i;
		}

		return ss;
	}

	public static String hash(File f)
	{
		try
		{
			MessageDigest d = MessageDigest.getInstance("SHA-256");
			FileInputStream fin = new FileInputStream(f);
			DigestInputStream din = new DigestInputStream(fin, d);
			VIO.fullTransfer(din, new VoidOutputStream(), 16891);
			String hash = bytesToHex(d.digest());
			din.close();

			return hash;
		}

		catch(NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}

		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}

		catch(IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public static String decompress(String gz) throws IOException
	{
		ByteArrayInputStream bin = new ByteArrayInputStream(Base64.getDecoder().decode(gz));
		GZIPInputStream gzi = new GZIPInputStream(bin);
		ByteArrayOutputStream boas = new ByteArrayOutputStream();
		VIO.fullTransfer(gzi, boas, 256);
		gzi.close();

		return new String(boas.toByteArray(), StandardCharsets.UTF_8);
	}

	public static String compress(String text) throws IOException
	{
		ByteArrayOutputStream boas = new ByteArrayOutputStream();
		GZIPOutputStream gzo = new CustomOutputStream(boas, 9);
		gzo.write(text.getBytes(StandardCharsets.UTF_8));
		gzo.flush();
		gzo.close();

		return Base64.getEncoder().encodeToString(boas.toByteArray());
	}
}
