package me.deftware.mixin.mixins.misc;

import me.deftware.client.framework.nbt.NbtList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(NBTTagCompound.class)
public class MixinNbt implements me.deftware.client.framework.nbt.NbtCompound {

    @Unique
    @Override
    public boolean contains(String key) {
        return ((NBTTagCompound) (Object) this).hasKey(key);
    }

    @Unique
    @Override
    public boolean contains(String key, int type) {
        return ((NBTTagCompound) (Object) this).hasKey(key, type);
    }

    @Unique
    @Override
    public me.deftware.client.framework.nbt.NbtCompound get(String key) {
        return (me.deftware.client.framework.nbt.NbtCompound) ((NBTTagCompound) (Object) this).getCompoundTag(key);
    }

    @Unique
    @Override
    public NbtList getList(String items, int type) {
        return (NbtList) ((NBTTagCompound) (Object) this).getTagList(items, type);
    }

    @Unique
    @Override
    public int getByte(String key) {
        return ((NBTTagCompound) (Object) this).getByte(key);
    }

    @Unique
    @Override
    public void put(String key, NbtList list) {
        ((NBTTagCompound) (Object) this).setTag(key, (NBTTagList) list);
    }

}
