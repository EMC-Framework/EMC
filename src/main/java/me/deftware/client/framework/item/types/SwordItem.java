package me.deftware.client.framework.item.types;

import net.minecraft.item.ItemSword;

/**
 * @author Deftware
 */
public class SwordItem extends WeaponItem {

	public SwordItem(net.minecraft.item.Item item) {
		super(item);
	}

	@Override
	public float getAttackDamage() {
		return ((ItemSword) item).getDamageVsEntity();
	}

}
