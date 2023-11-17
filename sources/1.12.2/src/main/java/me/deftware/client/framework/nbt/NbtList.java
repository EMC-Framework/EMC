package me.deftware.client.framework.nbt;

import net.minecraft.nbt.NBTTagList;

/**
 * @author Deftware
 */
public interface NbtList {

    static NbtList empty() {
        return (NbtList) new NBTTagList();
    }

    int size();

    NbtCompound getCompound(int index);

    void append(String text);

}
