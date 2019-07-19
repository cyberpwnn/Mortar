package mortar.api.inventory;

import mortar.bukkit.compatibility.MaterialEnum;
import org.bukkit.Material;

import mortar.api.world.MaterialBlock;
import mortar.util.text.C;

public class UIPaneDecorator extends UIStaticDecorator
{
	public UIPaneDecorator(C color)
	{
		super(new UIElement("c").setName(" ").setMaterial(new MaterialBlock(MaterialEnum.STAINED_GLASS_PANE.bukkitMaterial(), color.getItemMeta())));
	}
}
