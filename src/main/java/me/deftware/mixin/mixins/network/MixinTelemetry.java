package me.deftware.mixin.mixins.network;

import net.minecraft.client.telemetry.ClientTelemetryManager;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Disables Minecraft telemetry
 */
@Mixin(ClientTelemetryManager.class)
public class MixinTelemetry {

	@Redirect(method = "lambda$createEventSender$3", at = @At(value = "FIELD", target = "Lnet/minecraft/SharedConstants;IS_RUNNING_IN_IDE:Z", opcode = Opcodes.GETSTATIC))
	private static boolean onIsDevelopment() {
		return true;
	}

}
