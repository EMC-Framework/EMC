package me.deftware.mixin.mixins.misc;

import me.deftware.client.framework.nbt.NbtList;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(NbtCompound.class)
public class MixinNbt implements me.deftware.client.framework.nbt.NbtCompound {

    @Unique
    @Override
    public boolean contains(String key) {
        return ((NbtCompound) (Object) this).contains(key);
    }

    @Unique
    @Override
    public boolean contains(String key, int type) {
        return ((NbtCompound) (Object) this).contains(key, type);
    }

    @Unique
    @Override
    public me.deftware.client.framework.nbt.NbtCompound get(String key) {
        return (me.deftware.client.framework.nbt.NbtCompound) ((NbtCompound) (Object) this).get(key);
    }

    @Unique
    @Override
    public NbtList getList(String items, int type) {
        return (NbtList) ((NbtCompound) (Object) this).getList(items, type);
    }

    @Unique
    @Override
    public int getByte(String key) {
        return ((NbtCompound) (Object) this).getByte(key);
    }

    @Unique
    @Override
    public void put(String key, NbtList list) {
        ((NbtCompound) (Object) this).put(key, (net.minecraft.nbt.NbtList) list);
    }

}
