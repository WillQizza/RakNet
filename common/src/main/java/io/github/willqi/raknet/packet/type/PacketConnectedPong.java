package io.github.willqi.raknet.packet.type;

import lombok.Getter;
import lombok.Setter;

/**
 * Sent by the server in response to a {@link PacketConnectedPing}.
 */
@Getter
@Setter
public class PacketConnectedPong {
    private long requestTime;
    private long responseTime;
}
