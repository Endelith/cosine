package xyz.endelith.cosine.stream;

import java.util.function.Function;

import io.netty.buffer.ByteBuf;

public record TransformativeStreamCodec<T, S>(
        StreamCodec<T> inner,
        Function<T, S> to,
        Function<S, T> from
) implements StreamCodec<S> {

    @Override
    public void write(ByteBuf buffer, S value) {
        inner.write(buffer, from.apply(value));
    }

    @Override
    public S read(ByteBuf buffer) {
        T innerValue = inner.read(buffer);
        return to.apply(innerValue);
    }
}
