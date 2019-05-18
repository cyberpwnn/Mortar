package mortar.config.property;

import java.io.IOException;

public interface PropertyAdapter<T>
{
	public T write(PropertySet s) throws IOException;

	public PropertySet read(T t) throws IOException;
}
