package xyz.endelith.cosine.codec;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderException;

public final class CodecUtils {

    private CodecUtils() {}

    public static final int SEGMENT_BITS = 0x7F;
    public static final int CONTINUE_BIT = 0x80;
    public static final int MAXIMUM_VAR_INT_SIZE = 5;

    public static int[] uuidToIntArray(UUID uuid) {
        long most = uuid.getMostSignificantBits();
        long least = uuid.getLeastSignificantBits();

        return new int[] {
                (int) (most >>> 32),
                (int) most,
                (int) (least >>> 32),
                (int) least
        };
    }

    public static UUID intArrayToUuid(int[] array) {
        long most =
                ((long) array[0] << 32)
                        | ((long) array[1] & 0xFFFFFFFFL);

        long least =
                ((long) array[2] << 32)
                        | ((long) array[3] & 0xFFFFFFFFL);

        return new UUID(most, least);
    }

    public static int readVarInt(ByteBuf buffer) {
        int readable = buffer.readableBytes();
        if (readable == 0) {
            throw new Codec.DecodingException("Invalid VarInt");
        }

        int current = buffer.readByte();
        if ((current & CONTINUE_BIT) != CONTINUE_BIT) {
            return current;
        }

        int maxRead = Math.min(MAXIMUM_VAR_INT_SIZE, readable);
        int varInt = current & SEGMENT_BITS;

        for (int i = 1; i < maxRead; i++) {
            current = buffer.readByte();
            varInt |= (current & SEGMENT_BITS) << (i * 7);

            if ((current & CONTINUE_BIT) != CONTINUE_BIT) {
                return varInt;
            }
        }

        throw new Codec.DecodingException("Invalid VarInt");
    }

    public static void writeVarInt(ByteBuf buffer, int value) {

        // 1 byte
        if ((value & (-1 << 7)) == 0) {
            buffer.writeByte(value);
            return;
        }

        // 2 byte
        if ((value & (-1 << 14)) == 0) {
            int w = ((value & SEGMENT_BITS) | CONTINUE_BIT) << 8
                    | (value >>> 7);
            buffer.writeShort(w);
            return;
        }

        // 3 byte
        if ((value & (-1 << 21)) == 0) {
            buffer.writeByte((value & SEGMENT_BITS) | CONTINUE_BIT);
            buffer.writeByte(((value >>> 7) & SEGMENT_BITS) | CONTINUE_BIT);
            buffer.writeByte(value >>> 14);
            return;
        }

        // 4 byte
        if ((value & (-1 << 28)) == 0) {
            buffer.writeByte((value & SEGMENT_BITS) | CONTINUE_BIT);
            buffer.writeByte(((value >>> 7) & SEGMENT_BITS) | CONTINUE_BIT);
            buffer.writeByte(((value >>> 14) & SEGMENT_BITS) | CONTINUE_BIT);
            buffer.writeByte(value >>> 21);
            return;
        }

        // 5 byte
        buffer.writeByte((value & SEGMENT_BITS) | CONTINUE_BIT);
        buffer.writeByte(((value >>> 7) & SEGMENT_BITS) | CONTINUE_BIT);
        buffer.writeByte(((value >>> 14) & SEGMENT_BITS) | CONTINUE_BIT);
        buffer.writeByte(((value >>> 21) & SEGMENT_BITS) | CONTINUE_BIT);
        buffer.writeByte(value >>> 28);
    }

    public static String readString(ByteBuf buffer, int maxChars) {
        int maxSize = maxChars * 3;
        int size = readVarInt(buffer);

        if (size > maxSize) {
            throw new DecoderException(
                    "The received string was longer than the allowed "
                            + maxSize + " (" + size + " > " + maxSize + ")"
            );
        }

        if (size < 0) {
            throw new DecoderException("The received string's length was smaller than 0");
        }

        String str = buffer.toString(
                buffer.readerIndex(),
                size,
                StandardCharsets.UTF_8
        );

        buffer.readerIndex(buffer.readerIndex() + size);

        if (str.length() > maxChars) {
            throw new DecoderException(
                    "The received string was longer than the allowed ("
                            + str.length() + " > " + maxChars + ")"
            );
        }

        return str;
    }

    public static String readString(ByteBuf buffer) {
        return readString(buffer, Short.MAX_VALUE);
    }

    public static ByteBuf writeString(ByteBuf buffer, String text) {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        writeVarInt(buffer, bytes.length);
        buffer.writeBytes(bytes);
        return buffer;
    }

    public static byte[] toByteArraySafe(ByteBuf buf) {
        if (buf.hasArray()) {
            return buf.array();
        }

        byte[] bytes = new byte[buf.readableBytes()];
        buf.getBytes(buf.readerIndex(), bytes);
        return bytes;
    }

    public static ByteBuf toByteBuf(byte[] bytes) {
        return Unpooled.copiedBuffer(bytes);
    }
}
