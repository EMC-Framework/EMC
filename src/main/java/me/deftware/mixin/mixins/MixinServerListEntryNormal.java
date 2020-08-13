package me.deftware.mixin.mixins;

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

import java.util.Arrays;
import java.util.Collections;

@Mixin(MultiplayerServerListWidget.ServerEntry.class)
public class MixinServerListEntryNormal {

    private boolean sentEvent = false;

    @Final
    @Shadow
    private ServerInfo server;

    @Inject(method = "render", at = @At("HEAD"))
    public void render(int int_1, int int_2, int int_3, int int_4, int int_5, int int_6, int int_7, boolean boolean_1, float float_1, CallbackInfo ci) {
        if (server.ping > 1 && !sentEvent) {
            sentEvent = true;
            EventServerPinged event = new EventServerPinged(
                    new ChatMessage().fromString(server.label),
                    new ChatMessage().fromString(server.playerCountLabel),
                    new ChatMessage().fromString(server.version),
                    Collections.singletonList(new ChatMessage().fromString(server.playerListSummary)),
                    server.protocolVersion,
                    server.ping
            );
            event.broadcast();
            server.label = event.getServerMOTD().toString(true);
            server.playerListSummary = event.getPopulationInfo().get(0).toString(true);
            server.version = event.getGameVersion().toString(true);
            server.playerCountLabel = event.getPlayerList().toString(true);
            server.protocolVersion = event.getVersion();
            server.ping = event.getPingToServer();
        }
    }

}
