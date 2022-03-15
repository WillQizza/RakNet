package io.github.willqi.raknet.packet.handler;

import io.github.willqi.raknet.packet.data.PacketID;
import io.github.willqi.raknet.packet.exception.PacketDeserializationException;
import io.github.willqi.raknet.packet.exception.PacketSerializationException;
import io.github.willqi.raknet.packet.type.Packet;
import io.netty.buffer.ByteBuf;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class PacketUtils {

    private static final Map<Byte, PacketHandler<? extends Packet>> HANDLERS = new HashMap<>() {
        {
            this.put(PacketID.UNCONNECTED_PING, PacketHandlerUnconnectedPing.INSTANCE);
            this.put(PacketID.UNCONNECTED_OPEN_SPACE_PING, PacketHandlerUnconnectedPing.INSTANCE);
            this.put(PacketID.UNCONNECTED_PONG, PacketHandlerUnconnectedPong.INSTANCE);
        }
    };

    public static Packet deserialize(ByteBuf buffer) throws PacketDeserializationException {
        byte packetId = buffer.readByte();

        PacketHandler<? extends Packet> handler = HANDLERS.getOrDefault(packetId, null);
        if (handler == null) {
            throw new PacketDeserializationException("Unable to deserialize unknown packet with id: " + packetId);
        }

        return handler.deserialize(buffer);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void serialize(ByteBuf buffer, Packet packet) throws PacketSerializationException {
        byte packetId = packet.getId();

        PacketHandler handler = HANDLERS.getOrDefault(packetId, null);
        if (handler == null) {
            throw new PacketSerializationException("Unable to serialize unknown packet with id: " + packetId);
        }

        buffer.writeByte(packetId);
        handler.serialize(buffer, packet);
    }

}
