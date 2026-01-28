package xyz.endelith.cosine.stream;

import java.util.BitSet;
import io.netty.buffer.ByteBuf;

public record FixedBitSetStreamCodec(int length) implements StreamCodec<BitSet> {

    @Override
    public void write(ByteBuf buffer, BitSet value) {
        if (value.length() > this.length) {
            throw new IllegalStateException(String.format(
                "BitSet is larger than expected size (%s > %s)",
                value.length(), this.length
            ));
        } else {
            byte[] array = value.toByteArray();
            BYTE_ARRAY.write(buffer, array); 
        }
    }

    @Override
    public BitSet read(ByteBuf buffer) {
        byte[] array = BYTE_ARRAY.read(buffer);

        BitSet bitSet = BitSet.valueOf(array);
        if (bitSet.length() > this.length) {
            throw new IllegalStateException(String.format(
                "BitSet is larger than expected size (%s > %s)",
                bitSet.length(), this.length
            ));
        }

        return bitSet;
    }
}
