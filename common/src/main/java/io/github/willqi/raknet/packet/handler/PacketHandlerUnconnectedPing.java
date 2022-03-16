package io.github.willqi.raknet.packet.handler;

import io.github.willqi.raknet.packet.exception.PacketSerializationException;
import io.github.willqi.raknet.packet.handler.util.PacketIOUtils;
import io.github.willqi.raknet.packet.type.PacketUnconnectedPing;
import io.netty.buffer.ByteBuf;

public class PacketHandlerUnconnectedPing implements PacketHandler<PacketUnconnectedPing> {

    public static PacketHandler<PacketUnconnectedPing> INSTANCE = new PacketHandlerUnconnectedPing();


    @Override
    public PacketUnconnectedPing deserialize(ByteBuf buffer) {
        PacketUnconnectedPing pingPacket = new PacketUnconnectedPing();
        pingPacket.setTime(buffer.readLong());
        pingPacket.setMagic(PacketIOUtils.readMagic(buffer));
        pingPacket.setGuid(buffer.readLong());

        return pingPacket;
    }

    @Override
    public void serialize(ByteBuf buffer, PacketUnconnectedPing packet) throws PacketSerializationException {
        buffer.writeLong(packet.getTime());
        PacketIOUtils.writeMagic(buffer, packet.getMagic());
        buffer.writeLong(packet.getGuid());
    }

}
