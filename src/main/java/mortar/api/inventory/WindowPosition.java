package mortar.api.inventory;

public class WindowPosition
{
	private int position;
	private int row;

	public WindowPosition(int position, int row)
	{
		this.position = position;
		this.row = row;
	}

	public int getPosition()
	{
		return position;
	}

	public void setPosition(int position)
	{
		this.position = position;
	}

	public int getRow()
	{
		return row;
	}

	public void setRow(int row)
	{
		this.row = row;
	}
}
