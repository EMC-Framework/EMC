package me.deftware.client.framework.wrappers.item;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.wrappers.item.items.IItemArmor;
import me.deftware.client.framework.wrappers.world.IBlock;
import me.deftware.client.framework.wrappers.world.IBlockPos;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;

public class IItemStack {

    public static final IItemStack EMPTY = new IItemStack(ItemStack.EMPTY);

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
        return new IItemStack(new ItemStack(Item.byRawId(Item.getRawIdByItem(stack.getStack().getItem())),
                Integer.parseInt(stack.getStack().toString().split("x")[0])));
    }

    public static boolean validName(String name) {
        return IItem.getByName(name) != null;
    }

    public void setNBT(String nbt) throws Exception {
        stack.setTag(StringNbtReader.parse(nbt));
    }

    public void enchantAll(int level) {
        for (Enchantment enchantment : Registry.ENCHANTMENT) {
            stack.addEnchantment(enchantment, level);
        }
    }

    public static ArrayList<String> getEnchantmentNames() {
        ArrayList<String> enchantNames = new ArrayList<>();

        for (Enchantment enchantment : Registry.ENCHANTMENT) {
            IEnchantment enchantmentObj = new IEnchantment(enchantment);
            enchantNames.add(enchantmentObj.getEnchantmentKey());
        }

        return enchantNames;
    }

    public void enchant(String name, int level) {
        for (Enchantment enchantment : Registry.ENCHANTMENT) {
            IEnchantment enchantmentData = new IEnchantment(enchantment);
            if (enchantmentData.getEnchantmentKey().equalsIgnoreCase(name)) {
                stack.addEnchantment(enchantment, level);
                break;
            }
        }
    }

    public static IItemStack read(INBTTagCompound compound) {
        return new IItemStack(ItemStack.fromTag(compound.getCompound()));
    }

    public void setStackDisplayName(String name) {
        CompoundTag nbttagcompound = stack.getOrCreateSubCompoundTag("display");
        nbttagcompound.putString("Name", TextComponent.Serializer.toJson(new TextComponent(name)).toString());
    }

    public boolean hasCompoundTag() {
        return stack.hasTag();
    }

    public void setTagInfo(String key, INBTTagList compound) {
        stack.setChildTag(key, compound.list);
    }

    public int getMaxStackSize() {
        return stack.getMaxAmount();
    }

    public static boolean areItemStackTagsEqual(IItemStack one, IItemStack two) {
        return ItemStack.areEqualIgnoreTags(one.getStack(), two.getStack());
    }

    public boolean isItemEqual(IItemStack stack) {
        return this.stack.isEqualIgnoreTags(stack.getStack());
    }

    public void setCount(int count) {
        stack.setAmount(count);
    }

    public int getCount() {
        return stack.getAmount();
    }

    public INBTTagCompound getTagCompound() {
        return new INBTTagCompound(stack.getTag());
    }

    public ItemStack getStack() {
        return stack;
    }

    public ChatMessage getDisplayName() {
        return new ChatMessage().fromText(stack.getDisplayName());
    }

    public int getItemID() {
        return Item.getRawIdByItem(stack.getItem());
    }

    public float getStrVsBlock(IBlockPos pos) {
        return stack.getBlockBreakingSpeed(MinecraftClient.getInstance().world.getBlockState(pos.getPos()));
    }

    public boolean isEmpty() {
        return stack.getItem() == Blocks.AIR.asItem();
    }

    public IItem getIItem() {
        if (stack.getItem() instanceof ArmorItem) {
            return new IItemArmor(stack.getItem());
        }
        return new IItem(stack.getItem());
    }

    public boolean hasEffect(IEffects ieffect) {
        for (StatusEffectInstance effect : PotionUtil.getPotionEffects(stack)) {
            if (effect.getEffectType() == ieffect.getEffect()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasEnchantment(IEnchantment enchantment) {
        return getEnchantmentLevel(enchantment) > 0;
    }

    public int getEnchantmentLevel(IEnchantment enchantment) {
        return EnchantmentHelper.getLevel(enchantment.getEnchantment(), stack);
    }

    public int getRarity() {
        if (stack.getRarity() == Rarity.COMMON) {
            return 0;
        } else if (stack.getRarity() == Rarity.UNCOMMON) {
            return 1;
        } else if (stack.getRarity() == Rarity.RARE) {
            return 2;
        } else if (stack.getRarity() == Rarity.EPIC) {
            return 3;
        }
        return 0;
    }

    public boolean isArmor() {
        return stack.getItem() instanceof ArmorItem;
    }

    public boolean isBow() {
        return stack.getItem() instanceof BowItem;
    }

    public int getEnchantmentLevel(int enchantID) {
        return EnchantmentHelper.getLevel(Enchantment.byRawId(enchantID), getStack());
    }

    public int getDamage() {
        return MathHelper.clamp(getRawDamage(), 0, getMaxDamage());
    }

    public int getRawDamage() {
        return stack.getDamage();
    }

    public int getMaxDamage() {
        return Math.max(getRawMaxDamage(), 0);
    }

    public int getRawMaxDamage() {
        return stack.getDurability();
    }

    public enum IEffects {

        InstantHealth(StatusEffects.INSTANT_HEALTH);

        private StatusEffect effect;

        IEffects(StatusEffect effect) {
            this.effect = effect;
        }

        public StatusEffect getEffect() {
            return effect;
        }

    }

}