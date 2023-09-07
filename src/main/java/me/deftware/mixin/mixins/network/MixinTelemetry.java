package me.deftware.mixin.mixins.network;

import net.minecraft.client.session.telemetry.TelemetryManager;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Disables Minecraft telemetry
 */
@Mixin(TelemetryManager.class)
public class MixinTelemetry {

	@Redirect(method = "computeSender", at = @At(value = "FIELD", target = "Lnet/minecraft/SharedConstants;isDevelopment:Z", opcode = Opcodes.GETSTATIC))
	private boolean onIsDevelopment() {
		return true;
	}

}
