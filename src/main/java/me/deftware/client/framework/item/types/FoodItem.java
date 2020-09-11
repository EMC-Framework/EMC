package me.deftware.client.framework.item.types;

import me.deftware.client.framework.item.Item;
import net.minecraft.item.ItemFood;

import java.util.Objects;

/**
 * @author Deftware
 */
public class FoodItem extends Item {

	public FoodItem(net.minecraft.item.Item item) {
		super(item);
	}

	@Override
	public ItemFood getMinecraftItem() {
		return (ItemFood) item;
	}

	public int getHunger() {
		return getMinecraftItem().getHealAmount(null);
	}

	public float getSaturation() {
		return getMinecraftItem().getSaturationModifier(null);
	}

}
