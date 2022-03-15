package io.github.willqi.raknet.packet.data;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PacketID {
    public static final byte CONNECTED_PING = 0x00;
    public static final byte UNCONNECTED_PING = 0x01;
    public static final byte UNCONNECTED_OPEN_SPACE_PING = 0x02;
    public static final byte CONNECTED_PONG = 0x03;
    public static final byte OPEN_CONNECTION_REQUEST1 = 0x05;
    public static final byte OPEN_CONNECTION_REPLY1 = 0x06;
    public static final byte OPEN_CONNECTION_REQUEST2 = 0x07;
    public static final byte OPEN_CONNECTION_REPLY2 = 0x08;
    public static final byte CONNECTION_REQUEST = 0x09;
    public static final byte CONNECTION_RESPONSE = 0x10;
    public static final byte NEW_INCOMING_CONNECTION = 0x13;
    public static final byte DISCONNECT = 0x15;
    public static final byte INCOMPATIBLE_PROTOCOL = 0x19;
    public static final byte UNCONNECTED_PONG = 0x1c;
    public static final byte SET_FRAME = (byte) 0x80;
    public static final byte GAME_PACKET = (byte) 0xfe;
    public static final byte NACK = (byte) 0xa0;
    public static final byte ACK = (byte) 0xc0;
}
