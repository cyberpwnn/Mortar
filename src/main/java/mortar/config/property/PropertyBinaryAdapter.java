package mortar.config.property;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;

import mortar.api.sql.CustomOutputStream;
import mortar.lang.collection.GList;

public class PropertyBinaryAdapter implements PropertyAdapter<ByteBuffer>
{
	@Override
	public ByteBuffer write(PropertySet s) throws IOException
	{
		ByteArrayOutputStream boas = new ByteArrayOutputStream();
		GZIPOutputStream gzo = new CustomOutputStream(boas, 9);
		DataOutputStream dos = new DataOutputStream(gzo);
		write(s, dos);
		dos.close();

		return ByteBuffer.wrap(boas.toByteArray());
	}

	private void write(PropertySet s, DataOutputStream dos) throws IOException
	{
		GList<String> keys = s.getKeys();

		for(String i : keys)
		{
			Object o = s.getRaw(i);
			byte t = PropertySet.getType(o);

			if(t < 0)
			{
				s.remove(i);
			}
		}

		dos.writeInt(keys.size());

		for(String i : keys)
		{
			Object o = s.getRaw(i);
			byte b = -1;
			dos.writeByte(PropertySet.getType(o));

			switch(b)
			{
				case 0:
					write((PropertySet) o, dos);
					break;
				case 1:
					dos.writeInt((Integer) o);
					break;
				case 2:
					dos.writeLong((Long) o);
					break;
				case 3:
					dos.writeDouble((Double) o);
					break;
				case 4:
					dos.writeFloat((Float) o);
					break;
				case 5:
					dos.writeShort((Short) o);
					break;
				case 6:
					dos.writeByte((Byte) o);
					break;
				case 7:
					dos.writeUTF((String) o);
					break;
				case 8:
					dos.writeUTF(((UUID) o).toString());
					break;
			}
		}
	}

	@Override
	public PropertySet read(ByteBuffer t) throws IOException
	{
		// TODO Auto-generated method stub
		return null;
	}
}
