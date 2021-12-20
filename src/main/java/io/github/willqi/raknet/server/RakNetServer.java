package io.github.willqi.raknet.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a RakNetServer and is the entry class to creating a RakNet server
 */
public class RakNetServer {

    private Bootstrap udpServer;
    private Channel udpChannel;
    private EventLoopGroup serverEventLoopGroup;


    public static void main(String[] args) throws InterruptedException {
        RakNetServer rakNetServer = new RakNetServer();
        rakNetServer.bind(new InetSocketAddress(8000));

        while (true) {
            Thread.sleep(10000);
        }
    }

    public void bind(InetSocketAddress socketAddress) {
        if (this.udpServer != null) {
            throw new IllegalStateException("This RakNet server is already binded.");
        }

        this.serverEventLoopGroup = new NioEventLoopGroup();
        this.udpServer = new Bootstrap()
                .group(this.serverEventLoopGroup)
                .channel(NioDatagramChannel.class)
                .localAddress(socketAddress)
                .handler(new SimpleChannelInboundHandler<DatagramPacket>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket message) {
                        byte packetId = message.content().readByte();
                        switch (packetId) {
                            case 0x01:
                            case 0x02:
                                long pingTime = message.content().readLong();
                                long guid = ThreadLocalRandom.current().nextLong();
                                byte[] magic = new byte[]{0, -1, -1, 0, -2, -2, -2, -2, -3, -3, -3, -3, 18, 52, 86, 120};
                                String ad = "MCPE;Dedicated Server;475;1.17;0;10;" + guid + ";Bedrock level";

                                ByteBuf buffer = ByteBufAllocator.DEFAULT.ioBuffer();
                                buffer.writeByte(0x1c);
                                buffer.writeLong(pingTime);
                                buffer.writeLong(guid);
                                buffer.writeBytes(magic);
                                buffer.writeShort(ad.getBytes(StandardCharsets.UTF_8).length);
                                buffer.writeBytes(ad.getBytes(StandardCharsets.UTF_8));
                                ctx.writeAndFlush(new DatagramPacket(buffer, message.sender()));
                                break;
                        }
                    }
                });

        this.udpChannel = this.udpServer.bind().channel();
    }

    public void shutdown() throws InterruptedException {
        this.udpChannel.close().sync();

        this.udpServer = null;
        this.udpChannel = null;
        this.serverEventLoopGroup = null;
    }

}
