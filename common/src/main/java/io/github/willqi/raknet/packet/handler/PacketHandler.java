package io.github.willqi.raknet.packet.handler;

import io.github.willqi.raknet.packet.exception.PacketDeserializationException;
import io.github.willqi.raknet.packet.exception.PacketSerializationException;
import io.github.willqi.raknet.packet.type.Packet;
import io.netty.buffer.ByteBuf;

public interface PacketHandler<T extends Packet> {

    T deserialize(ByteBuf buffer) throws PacketDeserializationException;

    void serialize(ByteBuf buffer, T packet) throws PacketSerializationException;

}
