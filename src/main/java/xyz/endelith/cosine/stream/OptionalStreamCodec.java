package xyz.endelith.cosine.stream;

import io.netty.buffer.ByteBuf;

public record OptionalStreamCodec<T>(StreamCodec<T> inner) implements StreamCodec<T> {

    @Override
    public void write(ByteBuf buffer, T value) {
        boolean isPresent = value != null;
        StreamCodec.BOOLEAN.write(buffer, isPresent);
        if (isPresent) {
            inner.write(buffer, value);
        }
    }

    @Override
    public T read(ByteBuf buffer) {
        boolean isPresent = StreamCodec.BOOLEAN.read(buffer);
        return isPresent ? inner.read(buffer) : null;
    }
}
