package me.deftware.mixin.mixins.network;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.event.events.EventServerPinged;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.network.ServerInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(MultiplayerServerListWidget.ServerEntry.class)
public class MixinServerListEntryNormal {

    private boolean sentEvent = false;

    @Final
    @Shadow
    private ServerInfo server;

    @Inject(method = "render", at = @At("HEAD"))
    public void render(int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta, CallbackInfo ci) {
        if (server.ping > 1 && !sentEvent) {
            sentEvent = true;
            List<ChatMessage> populationInfo = new ArrayList<>();
            if (server.playerListSummary != null) {
                populationInfo.add(new ChatMessage().fromString(server.playerListSummary));
            }
            EventServerPinged event = new EventServerPinged(
                    new ChatMessage().fromString(server.label),
                    new ChatMessage().fromString(server.playerCountLabel),
                    new ChatMessage().fromString(server.version),
                    populationInfo,
                    server.protocolVersion,
                    server.ping
            );
            event.broadcast();
            server.label = event.getServerMOTD().toString(true);
            if (server.playerListSummary != null) {
                server.playerListSummary = event.getPopulationInfo().get(0).toString(true);
            }
            server.version = event.getGameVersion().toString(true);
            server.playerCountLabel = event.getPlayerList().toString(true);
            server.protocolVersion = event.getVersion();
            server.ping = event.getPingToServer();
        }
    }

}
