package me.deftware.client.framework.item;

import net.minecraft.init.Items;
import net.minecraft.item.*;

/**
 * @author Deftware
 */
public enum ItemType {

	ItemPotion, ItemFishingRod, ItemFood, ItemSword, ItemTool, ItemNameTag, ItemBlock, ItemHoe, SplashPotion,
	ItemSoup, ItemShulkerBox, WritableBook;

	public boolean instanceOf(Item emcItem) {
		net.minecraft.item.Item item = emcItem.getMinecraftItem();
		if (this.equals(ItemFishingRod)) {
			return item instanceof ItemFishingRod;
		} else if (this.equals(ItemPotion)) {
			return item instanceof ItemPotion;
		} else if (this.equals(SplashPotion)) {
			return item == Items.SPLASH_POTION;
		} else if (this.equals(ItemFood)) {
			return item.getGroup() == ItemGroup.FOOD;
		} else if (this.equals(ItemSword)) {
			return item instanceof ItemSword;
		} else if (this.equals(ItemTool)) {
			return item instanceof ItemTool;
		} else if (this.equals(ItemNameTag)) {
			return item instanceof ItemNameTag;
		} else if (this.equals(ItemBlock)) {
			return item instanceof ItemBlock;
		} else if (this.equals(ItemSoup)) {
			return item instanceof ItemSoup;
		} else if (this.equals(WritableBook)) {
			return item instanceof ItemWritableBook;
		} else if (this.equals(ItemHoe)) {
			return item instanceof ItemHoe;
		} else if (this.equals(ItemShulkerBox)) {
			return item instanceof ItemBlock && item.getTranslationKey().contains("shulker_box");
		}
		return false;
	}
	
}
