package io.github.willqi.raknet.packet.type;

import lombok.Getter;
import lombok.Setter;

/**
 * Sent by the client to ping the server while connected.
 */
@Getter
@Setter
public class PacketConnectedPing {
    private long time;
}
