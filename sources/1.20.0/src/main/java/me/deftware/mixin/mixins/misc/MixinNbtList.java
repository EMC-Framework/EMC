package me.deftware.mixin.mixins.misc;

import me.deftware.client.framework.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(NbtList.class)
public class MixinNbtList implements me.deftware.client.framework.nbt.NbtList {

    @Unique
    @Override
    public int size() {
        return ((NbtList) (Object) this).size();
    }

    @Unique
    @Override
    public NbtCompound getCompound(int index) {
        return (NbtCompound) ((NbtList) (Object) this).getCompound(index);
    }

    @Unique
    @Override
    public void append(String text) {
        ((NbtList) (Object) this).add(NbtString.of(text));
    }

}
