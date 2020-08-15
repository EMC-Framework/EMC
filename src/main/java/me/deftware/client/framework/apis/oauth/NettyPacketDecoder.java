package me.deftware.client.framework.apis.oauth;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.minecraft.network.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.io.IOException;
import java.util.List;

public class NettyPacketDecoder extends ByteToMessageDecoder {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Marker RECEIVED_PACKET_MARKER;
    private final EnumPacketDirection direction;

    public NettyPacketDecoder(EnumPacketDirection p_i45999_1_) {
        this.direction = p_i45999_1_;
    }

    protected void decode(ChannelHandlerContext p_decode_1_, ByteBuf p_decode_2_, List<Object> p_decode_3_) throws IOException, InstantiationException, IllegalAccessException, Exception {
        if (p_decode_2_.readableBytes() != 0) {
            PacketBuffer lvt_4_1_ = new PacketBuffer(p_decode_2_);
            int lvt_5_1_ = lvt_4_1_.readVarIntFromBuffer();
            Packet<?> lvt_6_1_ = ((EnumConnectionState)p_decode_1_.channel().attr(NetworkManager.attrKeyConnectionState).get()).getPacket(this.direction, lvt_5_1_);
            if (lvt_6_1_ == null) {
                throw new IOException("Bad packet id " + lvt_5_1_);
            } else {
                lvt_6_1_.readPacketData(lvt_4_1_);
                if (lvt_4_1_.readableBytes() > 0) {
                    throw new IOException("Packet " + ((EnumConnectionState)p_decode_1_.channel().attr(NetworkManager.attrKeyConnectionState).get()).getId() + "/" + lvt_5_1_ + " (" + lvt_6_1_.getClass().getSimpleName() + ") was larger than I expected, found " + lvt_4_1_.readableBytes() + " bytes extra whilst reading packet " + lvt_5_1_);
                } else {
                    p_decode_3_.add(lvt_6_1_);
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug(RECEIVED_PACKET_MARKER, " IN: [{}:{}] {}", p_decode_1_.channel().attr(NetworkManager.attrKeyConnectionState).get(), lvt_5_1_, lvt_6_1_.getClass().getName());
                    }

                }
            }
        }
    }

    static {
        RECEIVED_PACKET_MARKER = MarkerManager.getMarker("PACKET_RECEIVED", NetworkManager.logMarkerPackets);
    }
}
