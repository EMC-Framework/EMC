package me.deftware.client.framework.nbt;

import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

/**
 * @author Deftware
 */
public class NbtCompound {

	private final NBTTagCompound compound;

	public NbtCompound(NBTTagCompound compound) {
		this.compound = compound;
	}

	public NbtCompound() {
		this.compound = new NBTTagCompound();
	}

	public boolean isValid() {
		return compound != null;
	}

	public boolean contains(String key) {
		return compound.hasKey(key);
	}

	public boolean getBoolean(String key) {
		return getMinecraftCompound().getBoolean(key);
	}

	public UUID getUUID(String key) {
		//if (getMinecraftCompound().hasKey(key))
			// return getMinecraftCompound().get(key); FIXME
		return null;
	}

	public boolean contains(String key, int type) {
		return compound.hasKey(key, type);
	}

	public NbtCompound get(String key) {
		return new NbtCompound(compound.getCompoundTag(key));
	}

	public NBTTagCompound getMinecraftCompound() {
		return compound;
	}

	public void setTagInfo(String key, NbtList list) {
		compound.setTag(key, list.getMinecraftListTag());
	}

}
