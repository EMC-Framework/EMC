package me.deftware.client.framework.item;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.item.types.BlockItem;
import me.deftware.client.framework.item.types.FishingRodItem;
import me.deftware.client.framework.item.types.PotionItem;
import me.deftware.client.framework.item.types.RangedWeaponItem;
import me.deftware.client.framework.item.types.ToolItem;
import me.deftware.client.framework.item.types.TridentItem;
import me.deftware.client.framework.item.types.*;
import me.deftware.client.framework.registry.BlockRegistry;
import me.deftware.client.framework.world.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.registry.IRegistry;

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
		} else if (item.getGroup() == ItemGroup.FOOD && !(item instanceof ItemBlock)) {
			return new FoodItem(item);
		} else if (item instanceof ItemBlock) {
			return new BlockItem(item);
		} else if (item instanceof ItemPotion) {
			return new PotionItem(item);
		} else if (item instanceof ItemFishingRod) {
			return new FishingRodItem(item);
		} else if (item instanceof ItemTrident) {
			return new TridentItem(item);
		} else if (item instanceof ItemBow || item instanceof ItemSnowball) {
			return new RangedWeaponItem(item);
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
		String key = getTranslationKey();
		if (key.startsWith("item.minecraft")) {
			key = key.substring("item.minecraft.".length());
		} else if (key.startsWith("block.minecraft")) {
			key = key.substring("block.minecraft.".length());
		}
		return key;
	}

	public boolean isAir() {
		return getMinecraftItem() == Blocks.AIR.asItem() || getMinecraftItem() == Blocks.CAVE_AIR.asItem();
	}

	public String getTranslationKey() {
		return item.getTranslationKey();
	}

	public int getID() {
		return IRegistry.ITEM.getId(item);
	}

	public ChatMessage getName() {
		return new ChatMessage().fromText(item.getName());
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
				|| item instanceof ItemEnderPearl || item instanceof ItemSplashPotion
				|| item instanceof ItemLingeringPotion || item instanceof ItemFishingRod;
	}

	public boolean instanceOf(ItemType type) {
		return type.instanceOf(this);
	}

	@Override
	public net.minecraft.item.Item getAsItem() {
		return item;
	}

	public Block getAsBlock() {
		if (instanceOf(ItemType.ItemBlock)) {
			return BlockRegistry.INSTANCE.getBlock(((net.minecraft.item.ItemBlock) item).getBlock());
		}
		return null;
	}

}
