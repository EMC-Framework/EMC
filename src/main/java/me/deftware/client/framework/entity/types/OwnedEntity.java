package me.deftware.client.framework.entity.types;

import javax.annotation.Nullable;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import java.util.UUID;

public class OwnedEntity extends LivingEntity {

	public OwnedEntity(net.minecraft.world.entity.Entity entity) {
		super(entity);
	}

	@Nullable
	public UUID getOwnerUUID() {
		var output = net.minecraft.world.level.storage.TagValueOutput.createWithContext(
				net.minecraft.util.ProblemReporter.DISCARDING,
				entity.registryAccess()
		);
		entity.saveWithoutId(output);
		return output.buildResult().read("Owner", UUIDUtil.CODEC).orElse(null);
	}

}
