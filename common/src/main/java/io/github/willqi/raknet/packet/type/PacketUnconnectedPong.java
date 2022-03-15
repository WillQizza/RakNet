package io.github.willqi.raknet.packet.type;

import io.github.willqi.raknet.packet.data.PacketID;
import lombok.Getter;
import lombok.Setter;

/**
 * Sent by the server in response to a {@link PacketUnconnectedPing}
 */
@Getter
@Setter
public class PacketUnconnectedPong extends Packet {
    private long time;
    private long guid;
    private byte[] magic = new byte[16];
    private String extra = "";

    @Override
    public byte getId() {
        return PacketID.UNCONNECTED_PONG;
    }

}
