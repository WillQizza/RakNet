package io.github.willqi.raknet.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;

/**
 * Represents a RakNetServer and is the entry class to creating a RakNet server
 */
public class RakNetServer {

    private Bootstrap nettyServer = null;


    public static void main(String[] args) throws InterruptedException {
        RakNetServer rakNetServer = new RakNetServer();
        rakNetServer.boot(new InetSocketAddress(8000));
    }

    public void boot(InetSocketAddress socketAddress) throws InterruptedException {
        if (this.nettyServer != null) {
            throw new IllegalStateException("This RakNet server is already booted up.");
        }
        RakNetServerHandler rakNetServerHandler = new RakNetServerHandler();
        NioEventLoopGroup serverEventLoop = new NioEventLoopGroup();
        try {
            this.nettyServer = new Bootstrap()
                    .group(serverEventLoop)
                    .channel(NioDatagramChannel.class)
                    .localAddress(socketAddress)
                    .handler(rakNetServerHandler);

            ChannelFuture serverFuture = this.nettyServer.bind().sync();
            serverFuture.channel().closeFuture().sync();
        } finally {
            serverEventLoop.shutdownGracefully().sync();
        }
    }

    public void shutdown() {
        // TODO: investigate how to shutdown a Netty server

    }

}
