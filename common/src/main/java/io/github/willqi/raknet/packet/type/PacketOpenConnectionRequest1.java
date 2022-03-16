package io.github.willqi.raknet.packet.type;

import io.github.willqi.raknet.packet.data.PacketID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PacketOpenConnectionRequest1 extends Packet {
    private byte[] magic = new byte[16];
    private int protocolVersion;
    private int maximumTransferUnit;

    @Override
    public byte getId() {
        return PacketID.OPEN_CONNECTION_REQUEST1;
    }

}
