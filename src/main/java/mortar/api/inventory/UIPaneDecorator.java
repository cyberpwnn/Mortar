package mortar.api.inventory;

import org.bukkit.Material;

import mortar.api.world.MaterialBlock;
import mortar.util.text.C;

public class UIPaneDecorator extends UIStaticDecorator
{
	public UIPaneDecorator(C color)
	{
		super(new UIElement("c").setName(" ").setMaterial(new MaterialBlock(Material.STAINED_GLASS_PANE, color.getItemMeta())));
	}
}