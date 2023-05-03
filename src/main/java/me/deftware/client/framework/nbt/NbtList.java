package me.deftware.client.framework.nbt;


/**
 * @author Deftware
 */
public interface NbtList {

    static NbtList empty() {
        return (NbtList) new net.minecraft.nbt.NbtList();
    }

    int size();

    NbtCompound getCompound(int index);

    void append(String text);

}
