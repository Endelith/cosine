package xyz.endelith.cosine.stream;

import io.netty.buffer.ByteBuf;
import xyz.endelith.cosine.codec.Codec;
import xyz.endelith.cosine.codec.CodecUtils;

import java.util.BitSet;

public record FixedBitSetStreamCodec(int length) implements StreamCodec<BitSet> {

    @Override
    public void write(ByteBuf buffer, BitSet value) {
        int setLength = value.length();
        if (setLength > length) {
            throw new Codec.EncodingException("BitSet is larger than expected size (" + setLength + " > " + length + ")");
        }
        byte[] bytes = value.toByteArray();
        StreamCodec.RAW_BYTES.write(buffer, CodecUtils.toByteBuf(bytes));
    }

    @Override
    public BitSet read(ByteBuf buffer) {
        int byteCount = (length + 7) / 8;
        ByteBuf byteBuf = buffer.readBytes(byteCount);
        byte[] array = CodecUtils.toByteArraySafe(byteBuf);
        return BitSet.valueOf(array);
    }
}
