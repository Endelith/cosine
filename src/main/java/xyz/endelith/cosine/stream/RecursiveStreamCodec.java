package xyz.endelith.cosine.stream;

import io.netty.buffer.ByteBuf;

import java.util.Objects;
import java.util.function.Function;

public class RecursiveStreamCodec<T> implements StreamCodec<T> {

    private final StreamCodec<T> delegate;

    public RecursiveStreamCodec(Function<StreamCodec<T>, StreamCodec<T>> self) {
        this.delegate = Objects.requireNonNull(self.apply(this));
    }

    public StreamCodec<T> delegate() {
        return delegate;
    }

    @Override
    public void write(ByteBuf buffer, T value) {
        delegate.write(buffer, value);
    }

    @Override
    public T read(ByteBuf buffer) {
        return delegate.read(buffer);
    }
}
