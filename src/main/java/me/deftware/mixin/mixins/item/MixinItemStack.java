package me.deftware.mixin.mixins.item;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import me.deftware.client.framework.entity.effect.AppliedEffect;
import me.deftware.client.framework.event.events.EventGetItemToolTip;
import me.deftware.client.framework.item.Enchantment;
import me.deftware.client.framework.item.Item;
import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.registry.EnchantmentRegistry;
import me.deftware.client.framework.world.block.BlockState;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.item.TooltipType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.AnvilScreenHandler;
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
        ((ItemStack) (Object) this)
                .getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT)
                .getEffects()
                .forEach(effect -> consumer.accept((AppliedEffect) effect));
    }

    @Unique
    @Override
    public void enchantments(BiConsumer<Integer, Enchantment> consumer) {
        var enchantments = EnchantmentHelper.getEnchantments((ItemStack) (Object) this);
        for (Object2IntMap.Entry<RegistryEntry<net.minecraft.enchantment.Enchantment>>
                entry : enchantments.getEnchantmentsMap()) {
            consumer.accept(entry.getIntValue(), EnchantmentRegistry.INSTANCE.lookup(entry.getKey().getKey().get()));
        }
    }

    @Unique
    @Override
    public void enchant(Enchantment enchantment, int level) {
        ((ItemStack) (Object) this).addEnchantment(enchantment.getEntry(), level);
    }

    @Unique
    @Override
    public Message getName() {
        return (Message) ((ItemStack) (Object) this).getName();
    }

    @Unique
    @Override
    public void setName(Message name) {
        ((ItemStack) (Object) this).set(DataComponentTypes.CUSTOM_NAME, (net.minecraft.text.Text) name);
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
    public float getMiningSpeed(BlockState state) {
        return ((ItemStack) (Object) this).getMiningSpeedMultiplier((net.minecraft.block.BlockState) state);
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

    @Unique
    private RegistryEntry<net.minecraft.enchantment.Enchantment> enchant$type;

    @Inject(method = "addEnchantment", at = @At("HEAD"))
    private void onEnchant(RegistryEntry<net.minecraft.enchantment.Enchantment> registryEntry, int level, CallbackInfo ci) {
        enchant$level = level;
        enchant$type = registryEntry;
    }

    /**
     * Removes (byte) cast from the level, allowing higher enchantment levels
     */
    @Redirect(method = "addEnchantment", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/enchantment/EnchantmentHelper;apply(Lnet/minecraft/item/ItemStack;Ljava/util/function/Consumer;)Lnet/minecraft/component/type/ItemEnchantmentsComponent;"))
    private ItemEnchantmentsComponent onEnchant$CreateNbt(ItemStack stack, Consumer<ItemEnchantmentsComponent.Builder> applier) {
        return EnchantmentHelper.apply((ItemStack) (Object) this, builder -> builder.add(enchant$type, enchant$level));
    }

    @Inject(method = "getTooltip", at = @At(value = "TAIL"))
    private void onGetTooltipFromItem(net.minecraft.item.Item.TooltipContext context, PlayerEntity player,
                                      TooltipType type, CallbackInfoReturnable<List<Text>> cir) {
        var list = cir.getReturnValue();
        new EventGetItemToolTip(list, (Item) ((ItemStack) (Object) this).getItem(),
                type.isAdvanced()).broadcast();
    }

}
