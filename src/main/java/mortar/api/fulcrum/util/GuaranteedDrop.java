package mortar.api.fulcrum.util;

public class GuaranteedDrop extends PotentialChance
{
	private int a;

	public GuaranteedDrop(int amount)
	{
		super(1D);
		this.a = amount;
	}

	@Override
	public int amount()
	{
		return a;
	}
}
