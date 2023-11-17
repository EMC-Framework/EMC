package me.deftware.mixin.mixins.misc;

import me.deftware.client.framework.nbt.NbtCompound;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ListTag.class)
public class MixinNbtList implements me.deftware.client.framework.nbt.NbtList {

    @Unique
    @Override
    public int size() {
        return ((ListTag) (Object) this).size();
    }

    @Unique
    @Override
    public NbtCompound getCompound(int index) {
        return (NbtCompound) ((ListTag) (Object) this).getCompound(index);
    }

    @Unique
    @Override
    public void append(String text) {
        ((ListTag) (Object) this).add(new StringTag(text));
    }

}
