package xyz.endelith.cosine.stream;

import java.util.Objects;

import io.netty.buffer.ByteBuf;

public record OptionalStreamCodec<T>(StreamCodec<T> parent) implements StreamCodec<T> {

    public OptionalStreamCodec {
        Objects.requireNonNull(parent, "parent");
    }

    @Override
    public void write(ByteBuf buffer, T value) {
        BOOLEAN.write(buffer, value != null);
        if (value != null) this.parent.write(buffer, value);
    }

    @Override
    public T read(ByteBuf buffer) {
        return BOOLEAN.read(buffer) ? this.parent.read(buffer) : null;
    }
}
