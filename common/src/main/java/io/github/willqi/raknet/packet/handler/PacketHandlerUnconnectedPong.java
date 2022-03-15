package io.github.willqi.raknet.packet.handler;

import io.github.willqi.raknet.packet.exception.PacketDeserializationException;
import io.github.willqi.raknet.packet.exception.PacketSerializationException;
import io.github.willqi.raknet.packet.type.PacketUnconnectedPong;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public class PacketHandlerUnconnectedPong implements PacketHandler<PacketUnconnectedPong> {

    public static final PacketHandler<PacketUnconnectedPong> INSTANCE = new PacketHandlerUnconnectedPong();


    @Override
    public PacketUnconnectedPong deserialize(ByteBuf buffer) {
        PacketUnconnectedPong pongPacket = new PacketUnconnectedPong();

        pongPacket.setTime(buffer.readLong());
        pongPacket.setGuid(buffer.readLong());

        byte[] magic = new byte[16];
        buffer.readBytes(magic);
        pongPacket.setMagic(magic);

        int extraLength = buffer.readUnsignedShort();
        byte[] extra = new byte[extraLength];
        buffer.readBytes(extra);
        pongPacket.setExtra(new String(extra));

        return pongPacket;
    }

    @Override
    public void serialize(ByteBuf buffer, PacketUnconnectedPong packet) throws PacketSerializationException {
        if (packet.getMagic().length != 16) {
            throw new PacketSerializationException("The magic provided is not 16 bytes.");
        }

        buffer.writeLong(packet.getTime());
        buffer.writeLong(packet.getGuid());
        buffer.writeBytes(packet.getMagic());
        buffer.writeShort(packet.getExtra().getBytes(StandardCharsets.UTF_8).length);
        buffer.writeBytes(packet.getExtra().getBytes(StandardCharsets.UTF_8));
    }

}
