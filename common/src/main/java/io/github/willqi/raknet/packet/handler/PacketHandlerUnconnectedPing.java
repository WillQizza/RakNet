package io.github.willqi.raknet.packet.handler;

import io.github.willqi.raknet.packet.exception.PacketSerializationException;
import io.github.willqi.raknet.packet.type.PacketUnconnectedPing;
import io.netty.buffer.ByteBuf;

public class PacketHandlerUnconnectedPing implements PacketHandler<PacketUnconnectedPing> {

    public static PacketHandler<PacketUnconnectedPing> INSTANCE = new PacketHandlerUnconnectedPing();


    @Override
    public PacketUnconnectedPing deserialize(ByteBuf buffer) {
        PacketUnconnectedPing pingPacket = new PacketUnconnectedPing();

        pingPacket.setTime(buffer.readLong());

        byte[] magic = new byte[16];
        buffer.readBytes(magic);
        pingPacket.setMagic(magic);

        pingPacket.setGuid(buffer.readLong());

        return pingPacket;
    }

    @Override
    public void serialize(ByteBuf buffer, PacketUnconnectedPing packet) throws PacketSerializationException {
        if (packet.getMagic().length != 16) {
            throw new PacketSerializationException("The magic provided is not 16 bytes.");
        }

        buffer.writeLong(packet.getTime());
        buffer.writeBytes(packet.getMagic());
        buffer.writeLong(packet.getGuid());
    }

}
