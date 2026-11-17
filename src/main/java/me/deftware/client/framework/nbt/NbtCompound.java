package me.deftware.client.framework.nbt;

/**
 * @author Deftware
 */
public interface NbtCompound {

	static NbtCompound create() {
		return (NbtCompound) new net.minecraft.nbt.CompoundTag();
	}

	boolean contains(String key);

	boolean contains(String key, int type);

	NbtCompound get(String key);

	NbtList getList(String items, int type);

	int getByte(String key);

	void put(String key, NbtList list);

}
