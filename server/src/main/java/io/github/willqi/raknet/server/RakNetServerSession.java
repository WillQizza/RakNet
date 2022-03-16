package io.github.willqi.raknet.server;

import io.github.willqi.raknet.packet.type.Packet;
import lombok.Getter;
import lombok.Setter;

public class RakNetServerSession {

    @Getter @Setter
    protected volatile RakNetServerSessionListener listener;

    public void sendPacket(Packet packet) {

    }

}
