package mortar.api.rift;

public class RiftException extends Exception
{
	private static final long serialVersionUID = 5174341769583745938L;

	public RiftException()
	{
		super();
	}

	public RiftException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RiftException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public RiftException(String message)
	{
		super(message);
	}

	public RiftException(Throwable cause)
	{
		super(cause);
	}

}
