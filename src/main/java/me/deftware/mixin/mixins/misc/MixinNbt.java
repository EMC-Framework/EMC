package me.deftware.mixin.mixins.misc;

import me.deftware.client.framework.nbt.NbtList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(CompoundTag.class)
public class MixinNbt implements me.deftware.client.framework.nbt.NbtCompound {

    @Unique
    @Override
    public boolean contains(String key) {
        return ((CompoundTag) (Object) this).contains(key);
    }

    @Unique
    @Override
    public boolean contains(String key, int type) {
        return ((CompoundTag) (Object) this).contains(key, type);
    }

    @Unique
    @Override
    public me.deftware.client.framework.nbt.NbtCompound get(String key) {
        return (me.deftware.client.framework.nbt.NbtCompound) ((CompoundTag) (Object) this).get(key);
    }

    @Unique
    @Override
    public NbtList getList(String items, int type) {
        return (NbtList) ((CompoundTag) (Object) this).getList(items, type);
    }

    @Unique
    @Override
    public int getByte(String key) {
        return ((CompoundTag) (Object) this).getByte(key);
    }

    @Unique
    @Override
    public void put(String key, NbtList list) {
        ((CompoundTag) (Object) this).put(key, (ListTag) list);
    }

}
