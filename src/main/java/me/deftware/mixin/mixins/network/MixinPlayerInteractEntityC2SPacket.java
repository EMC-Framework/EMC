package me.deftware.mixin.mixins.network;

import lombok.Getter;
import lombok.Setter;
import me.deftware.client.framework.network.packets.CPacketUseEntity;
import me.deftware.mixin.imp.IMixinPlayerInteractEntityC2SPacket;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerboundInteractPacket.class)
public class MixinPlayerInteractEntityC2SPacket implements IMixinPlayerInteractEntityC2SPacket {

    @Setter
    @Getter
    @Unique
    private CPacketUseEntity.Type actionType;


}
