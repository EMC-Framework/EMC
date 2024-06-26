package me.deftware.client.framework.item;

import me.deftware.client.framework.entity.effect.AppliedEffect;
import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.nbt.NbtCompound;
import me.deftware.client.framework.world.block.Block;
import me.deftware.client.framework.world.block.BlockState;
import net.minecraft.client.MinecraftClient;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author Deftware
 */
public interface ItemStack {

    ItemStack EMPTY = (ItemStack) net.minecraft.item.ItemStack.EMPTY;

    int getCount();

    int getMaxCount();

    void setCount(int count);

    Item getItem();

    void effects(Consumer<AppliedEffect> consumer);

    void enchantments(BiConsumer<Integer, Enchantment> consumer);

    void enchant(Enchantment enchantment, int level);

    Message getName();

    void setName(Message name);

    Rarity getRarity();

    int getDamage();

    int getMaxDamage();

    boolean isDamaged();

    boolean isDamageable();

    float getMiningSpeed(BlockState state);

    boolean isEmpty();

    boolean isItemEqual(ItemStack stack);

    static ItemStack of(Block block, int count) {
        return of(block.getItem(), count);
    }

    static ItemStack of(Item item, int count) {
        return (ItemStack) new net.minecraft.item.ItemStack((net.minecraft.item.Item) item, count);
    }

    static ItemStack of(NbtCompound compound) {
        var registry = MinecraftClient.getInstance().player.getRegistryManager();
        return (ItemStack) net.minecraft.item.ItemStack.fromNbtOrEmpty
                (registry, (net.minecraft.nbt.NbtCompound) compound);
    }

    enum Rarity {
        Common, Uncommon, Rare, Epic
    }

}
