package me.deftware.client.framework.apis.oauth;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.minecraft.network.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.io.IOException;

public class NettyPacketEncoder extends MessageToByteEncoder<Packet<?>> {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Marker RECEIVED_PACKET_MARKER;
    private final EnumPacketDirection direction;

    public NettyPacketEncoder(EnumPacketDirection p_i45998_1_) {
        this.direction = p_i45998_1_;
    }

    protected void encode(ChannelHandlerContext p_encode_1_, Packet<?> p_encode_2_, ByteBuf p_encode_3_) throws IOException, Exception {
        EnumConnectionState lvt_4_1_ = (EnumConnectionState)p_encode_1_.channel().attr(NetworkManager.attrKeyConnectionState).get();
        if (lvt_4_1_ == null) {
            throw new RuntimeException("ConnectionProtocol unknown: " + p_encode_2_.toString());
        } else {
            Integer lvt_5_1_ = lvt_4_1_.getPacketId(this.direction, p_encode_2_);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(RECEIVED_PACKET_MARKER, "OUT: [{}:{}] {}", p_encode_1_.channel().attr(NetworkManager.attrKeyConnectionState).get(), lvt_5_1_, p_encode_2_.getClass().getName());
            }

            if (lvt_5_1_ == null) {
                throw new IOException("Can't serialize unregistered packet");
            } else {
                PacketBuffer lvt_6_1_ = new PacketBuffer(p_encode_3_);
                lvt_6_1_.writeVarIntToBuffer(lvt_5_1_);

                try {
                    p_encode_2_.writePacketData(lvt_6_1_);
                } catch (Throwable var8) {
                    LOGGER.error(var8);
                }

            }
        }
    }

    static {
        RECEIVED_PACKET_MARKER = MarkerManager.getMarker("PACKET_SENT", NetworkManager.logMarkerPackets);
    }
}
