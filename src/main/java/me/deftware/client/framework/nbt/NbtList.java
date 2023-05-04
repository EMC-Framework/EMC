package me.deftware.client.framework.nbt;

import net.minecraft.nbt.ListTag;

/**
 * @author Deftware
 */
public interface NbtList {

    static NbtList empty() {
        return (NbtList) new ListTag();
    }

    int size();

    NbtCompound getCompound(int index);

    void append(String text);

}
