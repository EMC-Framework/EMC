package me.deftware.mixin.mixins.item;

import me.deftware.client.framework.entity.effect.AppliedEffect;
import me.deftware.client.framework.event.events.EventGetItemToolTip;
import me.deftware.client.framework.item.Enchantment;
import me.deftware.client.framework.item.Item;
import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.nbt.NbtCompound;
import me.deftware.client.framework.world.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public class MixinItemStack implements me.deftware.client.framework.item.ItemStack {

    @Unique
    @Override
    public int getCount() {
        return ((ItemStack) (Object) this).getCount();
    }

    @Unique
    @Override
    public int getMaxCount() {
        return ((ItemStack) (Object) this).getMaxCount();
    }

    @Unique
    @Override
    public void setCount(int count) {
        ((ItemStack) (Object) this).setCount(count);
    }

    @Unique
    @Override
    public Item getItem() {
        return (Item) ((ItemStack) (Object) this).getItem();
    }

    @Unique
    @Override
    public void effects(Consumer<AppliedEffect> consumer) {
        PotionUtil.getPotionEffects((ItemStack) (Object) this)
                .forEach(effect -> consumer.accept((AppliedEffect) effect));
    }

    @Unique
    @Override
    public void enchantments(BiConsumer<Integer, Enchantment> consumer) {
        EnchantmentHelper.getEnchantments((ItemStack) (Object) this)
                .forEach((key, value) -> consumer.accept(value, (Enchantment) key));
    }

    @Unique
    @Override
    public void enchant(Enchantment enchantment, int level) {
        ((ItemStack) (Object) this).addEnchantment((net.minecraft.enchantment.Enchantment) enchantment, level);
    }

    @Unique
    @Override
    public Message getName() {
        return (Message) ((ItemStack) (Object) this).getName();
    }

    @Unique
    @Override
    public void setName(Message name) {
        ((ItemStack) (Object) this).setCustomName((net.minecraft.text.Text) name);
    }

    @Unique
    @Override
    public Rarity getRarity() {
        return Rarity.values()[((ItemStack) (Object) this).getRarity().ordinal()];
    }

    @Unique
    @Override
    public int getDamage() {
        return ((ItemStack) (Object) this).getDamage();
    }

    @Unique
    @Override
    public int getMaxDamage() {
        return ((ItemStack) (Object) this).getMaxDamage();
    }

    @Unique
    @Override
    public boolean isDamaged() {
        return ((ItemStack) (Object) this).isDamaged();
    }

    @Unique
    @Override
    public boolean isDamageable() {
        return ((ItemStack) (Object) this).isDamageable();
    }

    @Unique
    @Override
    public NbtCompound getNbt() {
        return (NbtCompound) ((ItemStack) (Object) this).getTag();
    }

    @Unique
    @Override
    public void setNbt(NbtCompound nbt) {
        ((ItemStack) (Object) this).setTag((CompoundTag) nbt);
    }

    @Unique
    @Override
    public float getMiningSpeed(BlockState state) {
        return ((ItemStack) (Object) this).getMiningSpeed((net.minecraft.block.BlockState) state);
    }

    @Unique
    @Override
    public boolean isEmpty() {
        return ((ItemStack) (Object) this).isEmpty();
    }

    @Unique
    @Override
    public boolean isItemEqual(me.deftware.client.framework.item.ItemStack stack) {
        return ItemStack.areItemsEqual((ItemStack) (Object) this, (ItemStack) stack);
    }

    @Unique
    private int enchant$level;

    @Inject(method = "addEnchantment", at = @At("HEAD"))
    private void onEnchant(net.minecraft.enchantment.Enchantment enchantment, int level, CallbackInfo ci) {
        enchant$level = level;
    }

    /**
     * Removes (byte) cast from the level, allowing higher enchantment levels
     */
    @Redirect(method = "addEnchantment", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/nbt/CompoundTag;putShort(Ljava/lang/String;S)V"))
    private void onEnchant$CreateNbt(CompoundTag instance, String key, short value) {
        instance.putShort(key, (short) enchant$level);
    }

    @Inject(method = "getTooltip", at = @At(value = "TAIL"))
    private void onGetTooltipFromItem(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir) {
        List<Text> list = cir.getReturnValue();
        new EventGetItemToolTip(list, (Item) ((ItemStack) (Object) this).getItem(), context.isAdvanced()).broadcast();
    }

}
