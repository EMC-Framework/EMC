package me.deftware.mixin.mixins.network;

import me.deftware.client.framework.minecraft.Minecraft;
import me.deftware.client.framework.minecraft.ServerDetails;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConnectScreen.class)
public class MixinConnectScreen {

    @Inject(method = "connect(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/network/ServerAddress;Lnet/minecraft/client/network/ServerInfo;)V", at = @At("TAIL"))
    private void onConnect(MinecraftClient client, ServerAddress address, ServerInfo info, CallbackInfo ci) {
        ((Minecraft) client).setLastConnected((ServerDetails) info);
    }

}
