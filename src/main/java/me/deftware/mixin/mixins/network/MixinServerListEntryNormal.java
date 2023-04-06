package me.deftware.mixin.mixins.network;

import me.deftware.client.framework.event.events.EventServerPinged;
import net.minecraft.client.gui.ServerListEntryNormal;
import net.minecraft.client.multiplayer.ServerData;
import me.deftware.client.framework.message.MessageUtils;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerListEntryNormal.class)
public class MixinServerListEntryNormal {

    private boolean sentEvent = false;

    @Final
    @Shadow
    private ServerData server;

    @Inject(method = "drawEntry", at = @At("HEAD"))
    private void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, CallbackInfo ci) {
        if (server.pingToServer > 1 && !sentEvent) {
            sentEvent = true;
            EventServerPinged event = new EventServerPinged(
                    MessageUtils.parse(server.serverMOTD),
                    MessageUtils.parse(server.populationInfo),
                    MessageUtils.parse(server.gameVersion),
                    server.version,
                    server.pingToServer
            );
            event.broadcast();
            server.serverMOTD = ((IChatComponent) event.getServerMOTD()).getFormattedText();
            server.gameVersion = ((IChatComponent) event.getGameVersion()).getFormattedText();
            server.populationInfo = ((IChatComponent) event.getPlayerList()).getFormattedText();
            server.version = event.getVersion();
            server.pingToServer = event.getPingToServer();
        }
    }

}
