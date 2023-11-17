package me.deftware.client.framework.entity.types;

import net.minecraft.nbt.NbtCompound;

import javax.annotation.Nullable;
import java.util.UUID;

public class OwnedEntity extends LivingEntity {

	public OwnedEntity(net.minecraft.entity.Entity entity) {
		super(entity);
	}

	@Nullable
	public UUID getOwnerUUID() {
		return entity.writeNbt(new NbtCompound()).getUuid("Owner");
	}

}
