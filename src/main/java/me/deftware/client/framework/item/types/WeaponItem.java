package me.deftware.client.framework.item.types;

import net.minecraft.item.MiningToolItem;

/**
 * @author Deftware
 */
public class WeaponItem extends ToolItem {

	public WeaponItem(net.minecraft.item.Item item) {
		super(item);
	}

	public float getAttackDamage() {
		if (item instanceof MiningToolItem) {
			return ((MiningToolItem) item).getMaterial().getAttackDamage();
		}
		return 0;
	}

}
