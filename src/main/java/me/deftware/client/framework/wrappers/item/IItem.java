package me.deftware.client.framework.wrappers.item;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.mixin.imp.IMixinItemTool;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;

public class IItem {

    private Item item;

    private ItemStack stack;

    public IItem(String name) {
        item = getByName(name);
        stack = new ItemStack(item);
    }

    public IItem(Item item) {
        this.item = item;
        this.stack = new ItemStack(item);
    }

    public Item getItem() {
        return item;
    }

    public ChatMessage getName() {
        return new ChatMessage().fromString(item.getUnlocalizedName());
    }

    public String getTranslationKey() {
        return item.getUnlocalizedName();
    }

    public String getItemKey() {
        String key = getTranslationKey();
        if (key.startsWith("item.minecraft")) {
            key = key.substring("item.minecraft.".length());
        } else if (key.startsWith("block.minecraft")) {
            key = key.substring("block.minecraft.".length());
        }
        return key;
    }

    public int getID() {
        return Item.getIdFromItem(item);
    }

    public boolean isValidItem() {
        return item != null;
    }

    public float getAttackDamage() {
        return ((ItemSword) item).getDamageVsEntity() + 3.0F;
    }

    public float getDamageVsEntity() {
        return ((IMixinItemTool) item).getAttackDamage();
    }

    public boolean isThrowable() {
        return item instanceof ItemBow || item instanceof ItemSnowball || item instanceof ItemEgg
                || item instanceof ItemEnderPearl || item instanceof ItemSplashPotion
                || item instanceof ItemLingeringPotion || item instanceof ItemFishingRod;
    }

    public boolean instanceOf(IItemType type) {
        if (type.equals(IItemType.ItemFishingRod)) {
            return item instanceof ItemFishingRod;
        } else if (type.equals(IItemType.ItemPotion)) {
            return item instanceof ItemPotion;
        } else if (type.equals(IItemType.SplashPotion)) {
            return item == Items.SPLASH_POTION;
        } else if (type.equals(IItemType.ItemFood)) {
            return item instanceof ItemFood;
        } else if (type.equals(IItemType.ItemSword)) {
            return item instanceof ItemSword;
        } else if (type.equals(IItemType.ItemTool)) {
            return item instanceof ItemTool;
        } else if (type.equals(IItemType.ItemNameTag)) {
            return item instanceof ItemNameTag;
        } else if (type.equals(IItemType.ItemBlock)) {
            return item instanceof ItemBlock;
        } else if (type.equals(IItemType.ItemSoup)) {
            return item instanceof ItemSoup;
        } else if (type.equals(IItemType.WritableBook)) {
            return item instanceof ItemWritableBook;
        } else if (type.equals(IItemType.ItemHoe)) {
            return item instanceof ItemHoe;
        } else if (type.equals(IItemType.ItemShulkerBox)) {
            return item instanceof ItemBlock && item.getUnlocalizedName().contains("shulker_box");
        }
        return false;
    }

    public enum IItemType {
        ItemPotion, ItemFishingRod, ItemFood, ItemSword, ItemTool, ItemNameTag, ItemBlock, ItemHoe, SplashPotion,
        ItemSoup, ItemShulkerBox, WritableBook
    }

    protected static Item getByName(String id) {
        ResourceLocation resourceLocation = new ResourceLocation(id);
        if (Item.REGISTRY.containsKey(resourceLocation)) {
            return Item.REGISTRY.getObject(resourceLocation);
        }
        return null;
    }

}
