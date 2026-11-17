package me.deftware.mixin.mixins.item;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import me.deftware.client.framework.entity.effect.AppliedEffect;
import me.deftware.client.framework.event.events.EventGetItemToolTip;
import me.deftware.client.framework.item.Enchantment;
import me.deftware.client.framework.item.Item;
import me.deftware.client.framework.item.ItemAdapter;
import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.registry.EnchantmentRegistry;
import me.deftware.client.framework.world.block.BlockState;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
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
        return ((ItemStack) (Object) this).getMaxStackSize();
    }

    @Unique
    @Override
    public void setCount(int count) {
        ((ItemStack) (Object) this).setCount(count);
    }

    @Unique
    @Override
    public Item getItem() {
        return ItemAdapter.wrap(((ItemStack) (Object) this).getItem());
    }

    @Unique
    @Override
    public void effects(Consumer<AppliedEffect> consumer) {
        ((ItemStack) (Object) this)
                .getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
                .getAllEffects()
                .forEach(effect -> consumer.accept((AppliedEffect) effect));
    }

    @Unique
    @Override
    public void enchantments(BiConsumer<Integer, Enchantment> consumer) {
        var enchantments = EnchantmentHelper.getEnchantmentsForCrafting((ItemStack) (Object) this);
        for (Object2IntMap.Entry<Holder<net.minecraft.world.item.enchantment.Enchantment>>
                entry : enchantments.entrySet()) {
            try {
                var key = entry.getKey().unwrapKey();
                if (key.isPresent()) {
                    consumer.accept(entry.getIntValue(), EnchantmentRegistry.INSTANCE.lookup(key.get(), true));
                }
            } catch (IllegalStateException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    @Unique
    @Override
    public void enchant(Enchantment enchantment, int level) {
        ((ItemStack) (Object) this).enchant(enchantment.getEntry(), level);
    }

    @Unique
    @Override
    public Message getName() {
        return (Message) ((ItemStack) (Object) this).getHoverName();
    }

    @Unique
    @Override
    public void setName(Message name) {
        ((ItemStack) (Object) this).set(DataComponents.CUSTOM_NAME, (net.minecraft.network.chat.Component) name);
    }

    @Unique
    @Override
    public Rarity getRarity() {
        return Rarity.values()[((ItemStack) (Object) this).getRarity().ordinal()];
    }

    @Unique
    @Override
    public int getDamage() {
        return ((ItemStack) (Object) this).getDamageValue();
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
        return ((ItemStack) (Object) this).isDamageableItem();
    }

    @Unique
    @Override
    public float getMiningSpeed(BlockState state) {
        return ((ItemStack) (Object) this).getDestroySpeed((net.minecraft.world.level.block.state.BlockState) state);
    }

    @Unique
    @Override
    public boolean isEmpty() {
        return ((ItemStack) (Object) this).isEmpty();
    }

    @Unique
    @Override
    public boolean isItemEqual(me.deftware.client.framework.item.ItemStack stack) {
        return ItemStack.isSameItem((ItemStack) (Object) this, (ItemStack) stack);
    }

    @Unique
    private int enchant$level;

    @Unique
    private Holder<net.minecraft.world.item.enchantment.Enchantment> enchant$type;

    @Inject(method = "enchant", at = @At("HEAD"))
    private void onEnchant(Holder<net.minecraft.world.item.enchantment.Enchantment> registryEntry, int level, CallbackInfo ci) {
        enchant$level = level;
        enchant$type = registryEntry;
    }

    /**
     * Removes (byte) cast from the level, allowing higher enchantment levels
     */
    @Redirect(method = "enchant", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;updateEnchantments(Lnet/minecraft/world/item/ItemStack;Ljava/util/function/Consumer;)Lnet/minecraft/world/item/enchantment/ItemEnchantments;"))
    private ItemEnchantments onEnchant$CreateNbt(ItemStack stack, Consumer<ItemEnchantments.Mutable> applier) {
        return EnchantmentHelper.updateEnchantments((ItemStack) (Object) this, builder -> builder.upgrade(enchant$type, enchant$level));
    }

    @Inject(method = "getTooltipLines", at = @At(value = "TAIL"))
    private void onGetTooltipFromItem(net.minecraft.world.item.Item.TooltipContext context, Player player,
                                      TooltipFlag type, CallbackInfoReturnable<List<Component>> cir) {
        var list = cir.getReturnValue();
        new EventGetItemToolTip(list, (Item) ((ItemStack) (Object) this).getItem(),
                type.isAdvanced()).broadcast();
    }

}
