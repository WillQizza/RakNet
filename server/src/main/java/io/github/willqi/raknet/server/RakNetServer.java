package io.github.willqi.raknet.server;

import io.github.willqi.raknet.packet.data.PacketID;
import io.github.willqi.raknet.packet.exception.PacketDeserializationException;
import io.github.willqi.raknet.packet.exception.PacketSerializationException;
import io.github.willqi.raknet.packet.handler.util.PacketHandlerUtils;
import io.github.willqi.raknet.packet.type.Packet;
import io.github.willqi.raknet.packet.type.PacketOpenConnectionRequest1;
import io.github.willqi.raknet.packet.type.PacketUnconnectedPing;
import io.github.willqi.raknet.packet.type.PacketUnconnectedPong;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.Getter;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a RakNetServer and is the entry class to creating a RakNet server
 */
public class RakNetServer {

    private Bootstrap udpServer;
    private Channel udpChannel;
    private EventLoopGroup serverEventLoopGroup;

    @Getter
    protected final long guid = ThreadLocalRandom.current().nextLong();
    protected volatile String ad = "";

    private final Map<InetAddress, RakNetServerSession> sessions = new ConcurrentHashMap<>();
    protected volatile RakNetServerListener listener;


    public static void main(String[] args) throws InterruptedException {
        RakNetServer rakNetServer = new RakNetServer();
        rakNetServer.setAd("MCPE;Pizza Server;475;1.17;0;10;" + rakNetServer.getGuid() + ";Bedrock level");
        rakNetServer.bind(new InetSocketAddress(8000));

        while (true) {
            Thread.sleep(10000);
        }
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getAd() {
        return this.ad;
    }

    public void setListener(RakNetServerListener listener) {
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
                .handler(new NettyHandler());

        this.udpChannel = this.udpServer.bind().channel();
    }

    public void shutdown() throws InterruptedException {
        this.udpChannel.close().sync();

        this.udpServer = null;
        this.udpChannel = null;
        this.serverEventLoopGroup = null;
    }

    private Optional<RakNetServerSession> getSession(InetAddress address) {
        return Optional.ofNullable(this.sessions.getOrDefault(address, null));
    }

    private RakNetServerSession addSession(InetAddress address) {
        RakNetServerSession session = new RakNetServerSession();
        this.sessions.put(address, session);

        return session;
    }

    private boolean removeSession(InetAddress address) {
        return this.sessions.remove(address) != null;
    }


    private class NettyHandler extends SimpleChannelInboundHandler<DatagramPacket> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket message) {
            Optional<RakNetServerSession> session = RakNetServer.this.getSession(message.sender().getAddress());

            Packet packet;
            try {
                packet = PacketHandlerUtils.deserialize(message.content());
            } catch (PacketDeserializationException exception) {
                return;
            }


            switch (packet.getId()) {
                case PacketID.UNCONNECTED_PING, PacketID.UNCONNECTED_OPEN_SPACE_PING -> {
                    if (session.isPresent()) {
                        return; // Do not respond to these packets if the session exists.
                    }

                    PacketUnconnectedPing unconnectedPing = (PacketUnconnectedPing) packet;

                    PacketUnconnectedPong unconnectedPong = new PacketUnconnectedPong();
                    unconnectedPong.setTime(unconnectedPing.getTime());
                    unconnectedPong.setGuid(RakNetServer.this.getGuid());
                    unconnectedPong.setMagic(new byte[]{0, -1, -1, 0, -2, -2, -2, -2, -3, -3, -3, -3, 18, 52, 86, 120});
                    unconnectedPong.setExtra(RakNetServer.this.getAd());

                    ByteBuf buffer = ByteBufAllocator.DEFAULT.ioBuffer();
                    try {
                        PacketHandlerUtils.serialize(buffer, unconnectedPong);
                    } catch (PacketSerializationException exception) {
                        buffer.release();
                        return;
                    }

                    ctx.writeAndFlush(new DatagramPacket(buffer, message.sender()));
                }
                case PacketID.OPEN_CONNECTION_REQUEST1 -> {
                    if (session.isPresent()) {
                        return; // Do not respond to these packets if the session exists.
                    }

                    PacketOpenConnectionRequest1 openConnectionRequest1 = (PacketOpenConnectionRequest1) packet;
                }
            }
        }

    }

}
