package me.deftware.mixin.mixins.network;

import me.deftware.client.framework.message.Message;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;
import me.deftware.client.framework.event.events.EventServerPinged;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerSelectionList.OnlineServerEntry.class)
public class MixinServerListEntryNormal {

    private boolean sentEvent = false;

    @Final
    @Shadow
    private ServerData serverData;

    @Inject(method = "extractContent", at = @At("HEAD"))
    public void render(GuiGraphicsExtractor context, int mouseX, int mouseY, boolean hovered, float tickDelta, CallbackInfo ci) {
        if (serverData.ping > 1 && !sentEvent) {
            sentEvent = true;
            EventServerPinged event = new EventServerPinged(
                    (Message) serverData.motd,
                    (Message) serverData.status,
                    (Message) serverData.version,
                    serverData.protocol,
                    serverData.ping
            );
            event.broadcast();
            serverData.motd = (Component) event.getServerMOTD();
            serverData.version = (Component) event.getGameVersion();
            serverData.status = (Component) event.getPlayerList();
            serverData.protocol = event.getVersion();
            serverData.ping = event.getPingToServer();
        }
    }

}
