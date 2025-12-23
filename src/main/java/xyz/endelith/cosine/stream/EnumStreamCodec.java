package xyz.endelith.cosine.stream;

import io.netty.buffer.ByteBuf;

public record EnumStreamCodec<E extends Enum<E>>(Class<E> enumClass) implements StreamCodec<E> {

    @Override
    public void write(ByteBuf buffer, E value) {
        StreamCodec.VAR_INT.write(buffer, value.ordinal());
    }

    @Override
    public E read(ByteBuf buffer) {
        return enumClass.getEnumConstants()[StreamCodec.VAR_INT.read(buffer)];
    }
}
