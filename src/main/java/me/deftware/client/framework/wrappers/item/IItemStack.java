package me.deftware.client.framework.wrappers.item;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.wrappers.item.items.IItemArmor;
import me.deftware.client.framework.wrappers.world.IBlock;
import me.deftware.client.framework.wrappers.world.IBlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.*;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;

public class IItemStack {

    public static final IItemStack EMPTY = null;

    private ItemStack stack;

    public IItemStack(ItemStack stack) {
        this.stack = stack;
    }

    public IItemStack(IBlock block) {
        stack = new ItemStack(block.getBlock());
    }

    public IItemStack(IItem item) {
        stack = new ItemStack(item.getItem());
    }

    public IItemStack(IItem item, int amount) {
        stack = new ItemStack(item.getItem(), amount);
    }


    public IItemStack(String name) {
        stack = new ItemStack(IItem.getByName(name));
    }

    public IItemStack(String name, int amount) {
        stack = new ItemStack(IItem.getByName(name), amount);
    }

    public static IItemStack cloneWithoutEffects(IItemStack stack) {
        return new IItemStack(new ItemStack(Item.getItemById(Item.getIdFromItem(stack.getStack().getItem())),
                Integer.parseInt(stack.getStack().toString().split("x")[0])));
    }

    public static boolean validName(String name) {
        return IItem.getByName(name) != null;
    }

    public void setNBT(String nbt) throws Exception {
        stack.setTagCompound(JsonToNBT.getTagFromJson(nbt));
    }

    public void enchantAll(int level) {
        for (Object enchantment : Enchantment.REGISTRY) {
            stack.addEnchantment((Enchantment)enchantment, level);
        }
    }

    public static ArrayList<String> getEnchantmentNames() {
        ArrayList<String> enchantNames = new ArrayList<>();

        for (Object enchantment : Enchantment.REGISTRY) {
            IEnchantment enchantmentObj = new IEnchantment((Enchantment)enchantment);
            enchantNames.add(enchantmentObj.getEnchantmentKey());
        }

        return enchantNames;
    }

    public void enchant(String name, int level) {
        for (Object enchantment : Enchantment.REGISTRY) {
            IEnchantment enchantmentData = new IEnchantment((Enchantment)enchantment);
            if (enchantmentData.getEnchantmentKey().equalsIgnoreCase(name)) {
                stack.addEnchantment((Enchantment)enchantment, level);
                break;
            }
        }
    }

    public static IItemStack read(INBTTagCompound compound) {

        return new IItemStack(ItemStack.loadItemStackFromNBT(compound.getCompound()));
    }

    public void setStackDisplayName(String name) {
        NBTTagCompound nbttagcompound = stack.getSubCompound("display", true);
        nbttagcompound.setString("Name", ITextComponent.Serializer.componentToJson(new TextComponentString(name)));
    }

    public boolean hasCompoundTag() {
        return stack.hasTagCompound();
    }

    public void setTagInfo(String key, INBTTagList compound) {
        stack.setTagInfo(key, compound.list);
    }

    public int getMaxStackSize() {
        return stack != null ? stack.getMaxStackSize() : 0;
    }

    public static boolean areItemStackTagsEqual(IItemStack one, IItemStack two) {
        return ItemStack.areItemStackTagsEqual(one.getStack(), two.getStack());
    }

    public boolean isItemEqual(IItemStack stack) {
        return this.stack.isItemEqual(stack.getStack());
    }

    public void setCount(int count) {
        stack.stackSize = count;
    }

    public int getCount() {
        return stack != null ? stack.stackSize : 0;
    }

    public INBTTagCompound getTagCompound() {
        return new INBTTagCompound(stack.getTagCompound());
    }

    public ItemStack getStack() {
        return stack;
    }

    public ChatMessage getDisplayName() {
        return new ChatMessage().fromString(stack.getDisplayName());
    }

    public int getItemID() {
        try {
            return Item.getIdFromItem(stack.getItem());
        } catch (NullPointerException exception) {
            return 0;
        }
    }

    public float getStrVsBlock(IBlockPos pos) {
        return stack.getStrVsBlock(Minecraft.getMinecraft().theWorld.getBlockState(pos.getPos()));
    }

    public boolean isEmpty() {
        return stack.getItem() == Item.getItemFromBlock(Blocks.AIR);
    }

    public IItem getIItem() {
        if (stack.getItem() instanceof ItemArmor) {
            return new IItemArmor(stack.getItem());
        }
        return new IItem(stack.getItem());
    }

    public boolean hasEffect(IEffects ieffect) {
        for (PotionEffect effect : PotionUtils.getEffectsFromStack(stack)) {
            if (effect.getPotion() == ieffect.getEffect()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasEnchantment(IEnchantment enchantment) {
        return getEnchantmentLevel(enchantment) > 0;
    }

    public int getEnchantmentLevel(IEnchantment enchantment) {
        return EnchantmentHelper.getEnchantmentLevel(enchantment.getEnchantment(), stack);
    }

    public int getRarity() {
        if (stack.getRarity() == EnumRarity.COMMON) {
            return 0;
        } else if (stack.getRarity() == EnumRarity.UNCOMMON) {
            return 1;
        } else if (stack.getRarity() == EnumRarity.RARE) {
            return 2;
        } else if (stack.getRarity() == EnumRarity.EPIC) {
            return 3;
        }
        return 0;
    }

    public boolean isArmor() {
        return stack.getItem() instanceof ItemArmor;
    }

    public boolean isBow() {
        return stack.getItem() instanceof ItemBow;
    }

    public int getEnchantmentLevel(int enchantID) {
        return EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(enchantID), getStack());
    }

    public int getDamage() {
        return MathHelper.clamp_int(getRawDamage(), 0, getMaxDamage());
    }

    public int getRawDamage() {
        return stack.getItemDamage();
    }

    public int getMaxDamage() {
        return Math.max(getRawMaxDamage(), 0);
    }

    public int getRawMaxDamage() {
        return stack.getMaxDamage();
    }

    public enum IEffects {

        InstantHealth(MobEffects.INSTANT_HEALTH);

        private Potion effect;

        IEffects(Potion effect) {
            this.effect = effect;
        }

        public Potion getEffect() {
            return effect;
        }

    }

}