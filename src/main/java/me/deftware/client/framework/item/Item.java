package me.deftware.client.framework.item;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.item.types.BlockItem;
import me.deftware.client.framework.item.types.FishingRodItem;
import me.deftware.client.framework.item.types.PotionItem;
import me.deftware.client.framework.item.types.RangedWeaponItem;
import me.deftware.client.framework.item.types.ToolItem;
import me.deftware.client.framework.item.types.*;
import me.deftware.client.framework.registry.ItemRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.item.ItemStack;

/**
 * @author Deftware
 */
public class Item implements IItem {

	protected final net.minecraft.item.Item item;

	public static Item newInstance(net.minecraft.item.Item item) {
		if (item instanceof ItemArmor) {
			return new ArmourItem(item);
		} else if (item instanceof ItemBow) {
			return new me.deftware.client.framework.item.types.BowItem(item);
		} else if (item instanceof ItemSword) {
			return new me.deftware.client.framework.item.types.SwordItem(item);
		} else if (item instanceof ItemPickaxe || item instanceof ItemAxe) { // Mining tools
			return new WeaponItem(item);
		} else if (item instanceof ItemTool) {
			return new ToolItem(item);
		} else if (item instanceof ItemBlock) {
			return new BlockItem(item);
		} else if (item instanceof ItemPotion) {
			return new PotionItem(item);
		} else if (item instanceof ItemFishingRod) {
			return new FishingRodItem(item);
		} else if (item instanceof ItemBow || item instanceof ItemSnowball) {
			return new RangedWeaponItem(item);
		} else if (item.getCreativeTab() == CreativeTabs.tabFood) {
			return new FoodItem(item);
		}
		return new Item(item);
	}

	protected Item(net.minecraft.item.Item item) {
		this.item = item;
	}

	public net.minecraft.item.Item getMinecraftItem() {
		return item;
	}

	public String getIdentifierKey() {
		return ItemRegistry.INSTANCE.namesTranslated.getOrDefault(getUnlocalizedName(), getUnlocalizedName());
	}

	public boolean isAir() {
		return getMinecraftItem() == net.minecraft.item.Item.getItemFromBlock(Blocks.air);
	}

	public String getUnlocalizedName() {
		return item.getUnlocalizedName();
	}

	public int getID() {
		return net.minecraft.item.Item.getIdFromItem(item);
	}

	public ChatMessage getName() {
		return new ChatMessage().fromString(item.getUnlocalizedName());
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof Item) {
			return ((Item) object).getMinecraftItem() == getMinecraftItem() || getIdentifierKey().equals(((Item) object).getIdentifierKey());
		}
		return false;
	}

	public boolean isThrowable() {
		return item instanceof ItemBow || item instanceof ItemSnowball || item instanceof ItemEgg
				|| item instanceof ItemEnderPearl || (item instanceof ItemPotion && ItemPotion.isSplash(new ItemStack(item).getMetadata()))
				|| item instanceof ItemFishingRod;
	}

	public boolean instanceOf(ItemType type) {
		return type.instanceOf(this);
	}

	@Override
	public net.minecraft.item.Item getAsItem() {
		return item;
	}

}
