package xyz.endelith.cosine.stream;

import java.util.BitSet;
import java.util.EnumSet;
import java.util.Objects;
import io.netty.buffer.ByteBuf;

public record EnumSetStreamCodec<E extends Enum<E>>(
    Class<E> enumType,
    E[] values
) implements StreamCodec<EnumSet<E>> {

    public EnumSetStreamCodec {
        Objects.requireNonNull(enumType, "enum type");
        Objects.requireNonNull(values, "values");
    }

    @Override
    public void write(ByteBuf buffer, EnumSet<E> value) {
        BitSet bitSet = new BitSet(this.values.length);
        for (int i = 0; i < this.values.length; i++) {
            bitSet.set(i, value.contains(this.values[i]));
        }

        byte[] array = bitSet.toByteArray();
        BYTE_ARRAY.write(buffer, array);
    }

    @Override
    public EnumSet<E> read(ByteBuf buffer) {
        byte[] array = BYTE_ARRAY.read(buffer);
        BitSet bitSet = BitSet.valueOf(array);

        EnumSet<E> result = EnumSet.noneOf(this.enumType);
        for (int i = 0; i < this.values.length; i++) {
            if (bitSet.get(i)) {
                result.add(this.values[i]);
            }
        }

        return result;
    }
}
