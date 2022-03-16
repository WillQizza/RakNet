package io.github.willqi.raknet.packet.handler;

import io.github.willqi.raknet.packet.data.RakNetConstants;
import io.github.willqi.raknet.packet.exception.PacketDeserializationException;
import io.github.willqi.raknet.packet.exception.PacketSerializationException;
import io.github.willqi.raknet.packet.handler.util.PacketIOUtils;
import io.github.willqi.raknet.packet.type.PacketOpenConnectionRequest1;
import io.netty.buffer.ByteBuf;

public class PacketHandlerOpenConnectionRequest1 implements PacketHandler<PacketOpenConnectionRequest1> {

    public static final PacketHandler<PacketOpenConnectionRequest1> INSTANCE = new PacketHandlerOpenConnectionRequest1();

    @Override
    public PacketOpenConnectionRequest1 deserialize(ByteBuf buffer) throws PacketDeserializationException {
        PacketOpenConnectionRequest1 openConnectionRequest1 = new PacketOpenConnectionRequest1();
        openConnectionRequest1.setMagic(PacketIOUtils.readMagic(buffer));
        openConnectionRequest1.setProtocolVersion(buffer.readUnsignedByte());
        openConnectionRequest1.setMaximumTransferUnit(buffer.readableBytes() + RakNetConstants.MTU_PADDING);

        return openConnectionRequest1;
    }

    @Override
    public void serialize(ByteBuf buffer, PacketOpenConnectionRequest1 packet) throws PacketSerializationException {
        PacketIOUtils.writeMagic(buffer, packet.getMagic());
        buffer.writeByte(packet.getProtocolVersion());

        int mtu = packet.getMaximumTransferUnit() - RakNetConstants.MTU_PADDING;
        for (int i = 0; i < mtu; i++) {
            buffer.writeByte(0);
        }
    }

}
