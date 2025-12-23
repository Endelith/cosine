package xyz.endelith.cosine.stream;

import io.netty.buffer.ByteBuf;

public record DefaultStreamCodec<T>(StreamCodec<T> inner, T defaultValue) implements StreamCodec<T> {

    @Override
    public void write(ByteBuf buffer, T value) {
        StreamCodec.BOOLEAN.write(buffer, true);
        T valueToWrite = value != null ? value : defaultValue;
        inner.write(buffer, valueToWrite);
    }

    @Override
    public T read(ByteBuf buffer) {
        boolean isNull = StreamCodec.BOOLEAN.read(buffer);
        return !isNull ? inner.read(buffer) : defaultValue;
    }
}
