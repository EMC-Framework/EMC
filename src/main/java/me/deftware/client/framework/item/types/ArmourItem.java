package me.deftware.client.framework.item.types;

import me.deftware.client.framework.item.Item;
import net.minecraft.item.ItemArmor;

/**
 * @author Deftware
 */
public class ArmourItem extends Item {

	public ArmourItem(net.minecraft.item.Item item) {
		super(item);
	}

	@Override
	public ItemArmor getMinecraftItem() {
		return (ItemArmor) item;
	}

	public int getDamageReduceAmount() {
		return getMinecraftItem().getDamageReduceAmount();
	}
	
	public float getToughness() {
		return getMinecraftItem().getArmorMaterial().getToughness();
	}

	public int getTypeOrdinal() {
		return getMinecraftItem().getEquipmentSlot().ordinal();
	}

}
