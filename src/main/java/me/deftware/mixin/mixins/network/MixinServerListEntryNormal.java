package me.deftware.mixin.mixins.network;

import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.event.events.EventServerPinged;
import me.deftware.client.framework.message.MessageUtils;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
            EventServerPinged event = new EventServerPinged(
                    MessageUtils.parse(server.label),
                    MessageUtils.parse(server.playerCountLabel),
                    MessageUtils.parse(server.version),
                    server.protocolVersion,
                    server.ping
            );
            event.broadcast();
            server.label = ((Text) event.getServerMOTD()).asFormattedString();
            server.version = ((Text) event.getGameVersion()).asFormattedString();
            server.playerCountLabel = ((Text) event.getPlayerList()).asFormattedString();
            server.protocolVersion = event.getVersion();
            server.ping = event.getPingToServer();
        }
    }

}
