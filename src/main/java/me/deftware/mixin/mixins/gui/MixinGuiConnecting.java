package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.minecraft.Minecraft;
import me.deftware.client.framework.util.minecraft.ServerConnectionInfo;
import net.minecraft.client.gui.menu.ServerConnectingScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerConnectingScreen.class)
public class MixinGuiConnecting {

    @Inject(method = "method_2130", at = @At("HEAD"))
    private void connect(String ip, int port, CallbackInfo ci) {
        Minecraft.lastConnectedServer = new ServerConnectionInfo("Server", ip + ":" + port, false);
    }

}
