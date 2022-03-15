package io.github.willqi.raknet.server;

import io.github.willqi.raknet.packet.data.PacketID;
import io.github.willqi.raknet.packet.exception.PacketDeserializationException;
import io.github.willqi.raknet.packet.exception.PacketSerializationException;
import io.github.willqi.raknet.packet.handler.PacketUtils;
import io.github.willqi.raknet.packet.type.Packet;
import io.github.willqi.raknet.packet.type.PacketUnconnectedPing;
import io.github.willqi.raknet.packet.type.PacketUnconnectedPong;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a RakNetServer and is the entry class to creating a RakNet server
 */
public class RakNetServer {

    private Bootstrap udpServer;
    private Channel udpChannel;
    private EventLoopGroup serverEventLoopGroup;

    protected RakNetServerListener listener;


    public static void main(String[] args) throws InterruptedException {
        RakNetServer rakNetServer = new RakNetServer();
        rakNetServer.bind(new InetSocketAddress(8000));

        while (true) {
            Thread.sleep(10000);
        }
    }

    public synchronized void setListener(RakNetServerListener listener) {
        this.listener = listener;
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
                        Packet packet;
                        try {
                            packet = PacketUtils.deserialize(message.content());
                        } catch (PacketDeserializationException exception) {
                            return;
                        }

                        switch (packet.getId()) {
                            case PacketID.UNCONNECTED_PING, PacketID.UNCONNECTED_OPEN_SPACE_PING -> {
                                PacketUnconnectedPing unconnectedPing = (PacketUnconnectedPing) packet;

                                PacketUnconnectedPong unconnectedPong = new PacketUnconnectedPong();
                                unconnectedPong.setTime(unconnectedPing.getTime());
                                unconnectedPong.setGuid(ThreadLocalRandom.current().nextLong());
                                unconnectedPong.setMagic(new byte[]{0, -1, -1, 0, -2, -2, -2, -2, -3, -3, -3, -3, 18, 52, 86, 120});
                                unconnectedPong.setExtra("MCPE;Pizza Server;475;1.17;0;10;" + unconnectedPong.getGuid() + ";Bedrock level");

                                ByteBuf buffer = ByteBufAllocator.DEFAULT.ioBuffer();
                                try {
                                    PacketUtils.serialize(buffer, unconnectedPong);
                                } catch (PacketSerializationException exception) {
                                    return;
                                }

                                ctx.writeAndFlush(new DatagramPacket(buffer, message.sender()));
                            }
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
