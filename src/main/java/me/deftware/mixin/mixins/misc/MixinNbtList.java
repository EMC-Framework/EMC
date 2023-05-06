package me.deftware.mixin.mixins.misc;

import me.deftware.client.framework.nbt.NbtCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(NBTTagList.class)
public class MixinNbtList implements me.deftware.client.framework.nbt.NbtList {

    @Unique
    @Override
    public int size() {
        return ((NBTTagList) (Object) this).tagCount();
    }

    @Unique
    @Override
    public NbtCompound getCompound(int index) {
        return (NbtCompound) ((NBTTagList) (Object) this).getCompoundTagAt(index);
    }

    @Unique
    @Override
    public void append(String text) {
        ((NBTTagList) (Object) this).appendTag(new NBTTagString(text));
    }

}
