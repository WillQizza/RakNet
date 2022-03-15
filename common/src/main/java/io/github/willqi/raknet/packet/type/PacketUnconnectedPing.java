package io.github.willqi.raknet.packet.type;

import io.github.willqi.raknet.packet.data.PacketID;
import lombok.Getter;
import lombok.Setter;

/**
 * Sent by the client to ping a server while unconnected.
 */
@Getter
@Setter
public class PacketUnconnectedPing extends Packet {
    private long time;
    private byte[] magic = new byte[16];
    private long guid;

    @Override
    public byte getId() {
        return PacketID.UNCONNECTED_PING;
    }

}
