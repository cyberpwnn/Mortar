package mortar.api.fulcrum;

import mortar.compute.math.M;
import mortar.util.text.Alphabet;

public class ObfuscationSupplier
{
	int n = M.rand(11, 32);

	public String get(String origin)
	{
		if(!Fulcrum.obfuscate)
		{
			return origin;
		}

		Alphabet c1 = Alphabet.values()[M.rand(0, Alphabet.values().length - 1)];
		Alphabet c2 = Alphabet.values()[M.rand(0, Alphabet.values().length - 1)];
		Alphabet c3 = Alphabet.values()[M.rand(0, Alphabet.values().length - 1)];
		Alphabet c4 = Alphabet.values()[M.rand(0, Alphabet.values().length - 1)];
		Alphabet c5 = Alphabet.values()[M.rand(0, Alphabet.values().length - 1)];
		return c1.toString().substring(0, 1).toLowerCase() + c2.toString().substring(0, 1).toLowerCase() + c3.toString().substring(0, 1).toLowerCase() + Integer.toHexString(n += M.rand(3, 12)) + c4.toString().substring(0, 1).toLowerCase() + c5.toString().substring(0, 1).toLowerCase();
	}
}
