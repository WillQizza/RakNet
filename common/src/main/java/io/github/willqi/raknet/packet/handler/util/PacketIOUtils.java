package io.github.willqi.raknet.packet.handler.util;

import io.github.willqi.raknet.packet.exception.PacketSerializationException;
import io.netty.buffer.ByteBuf;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PacketIOUtils {

    public static byte[] readMagic(ByteBuf buffer) {
        byte[] magic = new byte[16];
        buffer.readBytes(magic);

        return magic;
    }

    public static void writeMagic(ByteBuf buffer, byte[] magic) throws PacketSerializationException {
        if (magic.length != 16) {
            throw new PacketSerializationException("The magic provided is not 16 bytes.");
        }

        buffer.writeBytes(magic);
    }

    public static String readString(ByteBuf buffer) {
        int extraLength = buffer.readUnsignedShort();
        byte[] extra = new byte[extraLength];
        buffer.readBytes(extra);

        return new String(extra);
    }

}
