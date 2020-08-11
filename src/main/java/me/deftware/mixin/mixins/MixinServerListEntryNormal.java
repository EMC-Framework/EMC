package me.deftware.mixin.mixins;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.event.events.EventServerPinged;
import net.minecraft.client.gui.ServerListEntryNormal;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;

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
                    new ChatMessage().fromString(server.serverMOTD),
                    new ChatMessage().fromString(server.populationInfo),
                    new ChatMessage().fromString(server.gameVersion),
                    Collections.singletonList(new ChatMessage().fromString(server.playerList)),
                    server.version,
                    server.pingToServer
            );
            event.broadcast();
            server.serverMOTD = event.getServerMOTD().toString(true);
            server.playerList = event.getPopulationInfo().get(0).toString(true);
            server.gameVersion = event.getGameVersion().toString(true);
            server.populationInfo = event.getPlayerList().toString(true);
            server.version = event.getVersion();
            server.pingToServer = event.getPingToServer();
        }
    }


}
