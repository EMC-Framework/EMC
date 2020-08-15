package me.deftware.client.framework.apis.oauth;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import net.minecraft.network.PacketBuffer;

import java.util.List;

public class NettyVarint21FrameDecoder extends ByteToMessageDecoder {
    public NettyVarint21FrameDecoder() {
    }

    protected void decode(ChannelHandlerContext p_decode_1_, ByteBuf p_decode_2_, List<Object> p_decode_3_) throws Exception {
        p_decode_2_.markReaderIndex();
        byte[] lvt_4_1_ = new byte[3];

        for(int lvt_5_1_ = 0; lvt_5_1_ < lvt_4_1_.length; ++lvt_5_1_) {
            if (!p_decode_2_.isReadable()) {
                p_decode_2_.resetReaderIndex();
                return;
            }

            lvt_4_1_[lvt_5_1_] = p_decode_2_.readByte();
            if (lvt_4_1_[lvt_5_1_] >= 0) {
                PacketBuffer lvt_6_1_ = new PacketBuffer(Unpooled.wrappedBuffer(lvt_4_1_));

                try {
                    int lvt_7_1_ = lvt_6_1_.readVarIntFromBuffer();
                    if (p_decode_2_.readableBytes() >= lvt_7_1_) {
                        p_decode_3_.add(p_decode_2_.readBytes(lvt_7_1_));
                        return;
                    }

                    p_decode_2_.resetReaderIndex();
                } finally {
                    lvt_6_1_.release();
                }

                return;
            }
        }

        throw new CorruptedFrameException("length wider than 21-bit");
    }
}
