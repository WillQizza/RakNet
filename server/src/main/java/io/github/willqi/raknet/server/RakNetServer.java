package io.github.willqi.raknet.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.SocketAddress;

/**
 * Represents a RakNetServer and is the entry class to creating a RakNet server
 */
public class RakNetServer {

    private ServerBootstrap nettyServer = null;
    private EventLoopGroup serverLoopGroup = null;
    private EventLoopGroup clientLoopGroup = null;


    public void boot(SocketAddress socketAddress) throws InterruptedException {
        if (this.nettyServer != null) {
            throw new IllegalStateException("This RakNet server is already booted up.");
        }

        this.serverLoopGroup = new NioEventLoopGroup();
        this.clientLoopGroup = new NioEventLoopGroup();

        this.nettyServer = new ServerBootstrap()
                .group(this.serverLoopGroup, this.clientLoopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) {
                        channel.pipeline().addLast(new RakNetServerHandler());
                    }
                });

        ChannelFuture closeFuture = this.nettyServer.bind(socketAddress).sync().channel().closeFuture();
        closeFuture.addListener(ignored -> {
            this.clientLoopGroup.shutdownGracefully();
            this.serverLoopGroup.shutdownGracefully();
        });
    }

    public void shutdown() {
        // TODO: investigate how to shutdown a Netty server
    }

}
