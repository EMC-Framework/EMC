package me.deftware.client.framework.nbt;

import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

/**
 * @author Deftware
 */
public class NbtList {

	private final NBTTagList list;

	public NbtList(NBTTagList list) {
		this.list = list;
	}

	public NbtList() {
		this(new NBTTagList());
	}

	public void appendTag(String tag) {
		list.add(new NBTTagString(tag));
	}

	public NBTTagList getMinecraftListTag() {
		return list;
	}
	
}
