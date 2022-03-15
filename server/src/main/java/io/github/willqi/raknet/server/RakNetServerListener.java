package io.github.willqi.raknet.server;

import java.net.InetSocketAddress;

public interface RakNetServerListener {

    default boolean onConnectionRequest(InetSocketAddress address) {
        return true;
    }

    void onConnection(RakNetServerSession session);

}
