package io.github.willqi.raknet.packet.handler;

import io.github.willqi.raknet.packet.exception.PacketSerializationException;
import io.github.willqi.raknet.packet.handler.util.PacketIOUtils;
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
        pongPacket.setMagic(PacketIOUtils.readMagic(buffer));
        pongPacket.setExtra(PacketIOUtils.readString(buffer));

        return pongPacket;
    }

    @Override
    public void serialize(ByteBuf buffer, PacketUnconnectedPong packet) throws PacketSerializationException {
        buffer.writeLong(packet.getTime());
        buffer.writeLong(packet.getGuid());
        PacketIOUtils.writeMagic(buffer, packet.getMagic());
        buffer.writeShort(packet.getExtra().getBytes(StandardCharsets.UTF_8).length);
        buffer.writeBytes(packet.getExtra().getBytes(StandardCharsets.UTF_8));
    }

}
